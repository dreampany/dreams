package com.dreampany.tools.ui.note.activity

import android.os.Bundle
import android.view.View
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.databinding.RecyclerActivityAdBinding
import com.dreampany.tools.manager.AdsManager
import com.dreampany.tools.ui.note.adapter.FastNoteAdapter
import com.dreampany.tools.ui.note.model.NoteItem
import com.dreampany.tools.ui.note.vm.NoteViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FavoriteNotesActivity : InjectActivity() {

    @Inject
    internal lateinit var ads: AdsManager

    private lateinit var bind: RecyclerActivityAdBinding
    private lateinit var vm: NoteViewModel
    private lateinit var adapter: FastNoteAdapter

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.recycler_activity_ad
    override val toolbarId: Int = R.id.toolbar
    override val menuRes: Int = R.menu.menu_search
    override val searchMenuItemId: Int = R.id.item_search

    override val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, "FavoriteNotesActivity")

            params.put(Constant.Event.activity(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        onRefresh()
        ads.loadBanner(this.javaClass.simpleName)
        ads.showInHouseAds(this)
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        ads.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ads.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::adapter.isInitialized) {
            var outState = outState
            outState = adapter.saveInstanceState(outState)
            super.onSaveInstanceState(outState)
            return
        }
        super.onSaveInstanceState(outState)
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        return false
    }

    override fun onRefresh() {
        loadNotes()
    }

    private fun loadNotes() {
        if (adapter.isEmpty)
            vm.loadFavoriteNotes()
        else
            bind.swipe.refresh(false)
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(NoteViewModel::class)

        vm.subscribes(this, { this.processResponse(it) })

        bind.swipe.init(this)
        bind.stateful.setStateView(
            StatefulLayout.State.EMPTY,
            R.layout.content_empty_favorite_notes
        )

    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastNoteAdapter(
            { currentPage ->
                Timber.v("CurrentPage: %d", currentPage)
                //onRefresh()
            }, this::onItemPressed
        )
        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler
        )
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<NoteItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<NoteItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        showDialogue(
            R.string.title_dialog_features,
            message = error.message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
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

    private fun onItemPressed(view: View, item: NoteItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                openUi(item)
            }
            R.id.button_favorite -> {

            }
            else -> {

            }
        }
    }

    private fun openUi(item: NoteItem) {
        val task = UiTask(
            Type.NOTE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.VIEW,
            item.input
        )
        open(NoteActivity::class, task)
    }
}