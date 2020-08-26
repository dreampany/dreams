package com.dreampany.tools.ui.note.activity

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.exts.init
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.toTint
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.note.NoteAction
import com.dreampany.tools.data.enums.note.NoteState
import com.dreampany.tools.data.enums.note.NoteSubtype
import com.dreampany.tools.data.enums.note.NoteType
import com.dreampany.tools.databinding.RecyclerActivityBinding
import com.dreampany.tools.ui.note.adapter.FastNoteAdapter
import com.dreampany.tools.ui.note.model.NoteItem
import com.dreampany.tools.ui.note.vm.NoteViewModel
import timber.log.Timber

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FavoriteNotesActivity : InjectActivity() {

    private lateinit var bind: RecyclerActivityBinding
    private lateinit var vm: NoteViewModel
    private lateinit var adapter: FastNoteAdapter

    override val homeUp: Boolean = true

    override val layoutRes: Int = R.layout.recycler_activity

    override val toolbarId: Int = R.id.toolbar

    override val menuRes: Int = R.menu.menu_search

    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        onRefresh()
    }

    override fun onStopUi() {
        adapter.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    /*override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(this, R.color.material_white)
    }*/

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
        bind = getBinding()
        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_favorite_notes)

        vm = createVm(NoteViewModel::class)
        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastNoteAdapter(
                { currentPage ->
                    Timber.v("CurrentPage: %d", currentPage)
                    //onRefresh()
                }, this::onItemPressed
            )
        }

        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler
        )
    }

    private fun processResponse(response: Response<NoteType, NoteSubtype, NoteState, NoteAction, List<NoteItem>>) {
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
            NoteType.NOTE,
            NoteSubtype.DEFAULT,
            NoteState.DEFAULT,
            NoteAction.VIEW,
            item.input
        )
        open(NoteActivity::class, task)
    }
}