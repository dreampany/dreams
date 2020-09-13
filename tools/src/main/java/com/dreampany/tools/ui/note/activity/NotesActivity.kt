package com.dreampany.tools.ui.note.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.note.NoteAction
import com.dreampany.tools.data.enums.note.NoteState
import com.dreampany.tools.data.enums.note.NoteSubtype
import com.dreampany.tools.data.enums.note.NoteType
import com.dreampany.tools.data.model.note.Note
import com.dreampany.tools.data.source.note.pref.NotePref
import com.dreampany.tools.databinding.RecyclerActivityAdBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.note.adapter.FastNoteAdapter
import com.dreampany.tools.ui.note.model.NoteItem
import com.dreampany.tools.ui.note.vm.NoteViewModel
import kotlinx.android.synthetic.main.content_recycler_ad.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NotesActivity : InjectActivity() {

    private val REQUEST_NOTE = 101

    @Inject
    internal lateinit var ad: AdManager

    @Inject
    internal lateinit var notePref: NotePref

    private lateinit var bind: RecyclerActivityAdBinding
    private lateinit var vm: NoteViewModel
    private lateinit var adapter: FastNoteAdapter

    override val homeUp: Boolean = true

    override val layoutRes: Int = R.layout.recycler_activity_ad
    override val menuRes: Int = R.menu.menu_notes
    override val toolbarId: Int = R.id.toolbar
    override val searchMenuItemId: Int = R.id.item_search

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constants.Param.PACKAGE_NAME, packageName)
            param.put(Constants.Param.VERSION_CODE, versionCode)
            param.put(Constants.Param.VERSION_NAME, versionName)
            param.put(Constants.Param.SCREEN, "NotesActivity")

            params.put(Constants.Event.ACTIVITY, param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        initAd()
        onRefresh()
        ad.loadBanner(this.javaClass.simpleName)
        ad.showInHouseAds(this)
    }

    override fun onStopUi() {
        adapter.destroy()
    }

    override fun onResume() {
        super.onResume()
        ad.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ad.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    /*override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(this, R.color.material_white)
        findMenuItemById(R.id.item_favorites).toTint(this, R.color.material_white)
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorites -> {
                openFavoritesUi()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        return false
    }

    override fun onRefresh() {
        if (adapter.isEmpty) {
            loadNotes()
            return
        }
        bind.swipe.refresh(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NOTE) {
            val task: UiTask<NoteType, NoteSubtype, NoteState, NoteAction, Note> =
                (data.task ?: return) as UiTask<NoteType, NoteSubtype, NoteState, NoteAction, Note>
            when (task.state) {
                NoteState.ADDED,
                NoteState.EDITED -> {
                    val input = task.input ?: return
                    vm.loadNote(input.id)
                }
            }
        }
    }

    private fun onItemPressed(view: View, item: NoteItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                openNoteUi(item)
            }
            R.id.button_edit -> {
                openEditNoteUi(item)
            }
            R.id.button_favorite -> {
                onFavoriteClicked(item)
            }
        }
    }

    private fun initAd() {
        ad.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun initUi() {
        bind = getBinding()
        bind.swipe.init(this)
        bind.fab.visible()
        bind.fab.setOnSafeClickListener { openAddNoteUi() }
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_notes)
        vm = createVm(NoteViewModel::class)
        vm.subscribe(this, Observer { this.processResponse(it) })
        vm.subscribes(this, Observer { this.processResponses(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastNoteAdapter(
                { currentPage ->
                    Timber.v("CurrentPage: %d", currentPage)
                    onRefresh()
                }, this::onItemPressed
            )
        }

        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler
        )
    }

    private fun loadNotes() {
        vm.loadNotes()
    }

    private fun processResponse(response: Response<NoteType, NoteSubtype, NoteState, NoteAction, NoteItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<NoteType, NoteSubtype, NoteState, NoteAction, NoteItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processResponses(response: Response<NoteType, NoteSubtype, NoteState, NoteAction, List<NoteItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<NoteType, NoteSubtype, NoteState, NoteAction, List<NoteItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResult(result: NoteItem?) {
        if (result != null) {
            adapter.updateItem(result)
        }
    }

    private fun processResults(result: List<NoteItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun onFavoriteClicked(item: NoteItem) {
        vm.toggleFavorite(item.input)
    }

    private fun openAddNoteUi() {
        val task = UiTask(
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.ADD,
            null as Note?
        )
        open(NoteActivity::class, task, REQUEST_NOTE)
    }

    private fun openEditNoteUi(item: NoteItem) {
        val task = UiTask(
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.EDIT,
            item.input
        )
        open(NoteActivity::class, task, REQUEST_NOTE)
    }

    private fun openNoteUi(item: NoteItem) {
        val task = UiTask(
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.VIEW,
            item.input
        )
        open(NoteActivity::class, task, REQUEST_NOTE)
    }

    private fun openFavoritesUi() {
         open(FavoriteNotesActivity::class)
    }
}