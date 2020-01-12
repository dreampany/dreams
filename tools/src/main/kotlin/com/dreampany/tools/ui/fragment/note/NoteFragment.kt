package com.dreampany.tools.ui.fragment.note

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.TextChangeListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.*
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.databinding.FragmentNoteBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.misc.NoteRequest
import com.dreampany.tools.ui.model.NoteItem
import com.dreampany.tools.ui.vm.note.NoteViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Roman-372 on 8/6/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class NoteFragment
@Inject constructor() :
    BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentNoteBinding

    private lateinit var vm: NoteViewModel
    private var edited: Boolean = false
    private var saved: Boolean = false
    private var noteTitle: String = Constants.Default.STRING
    private var noteDescription: String = Constants.Default.STRING

    override fun getLayoutId(): Int {
        return R.layout.fragment_note
    }

    override fun getMenuId(): Int {
        return R.menu.menu_note
    }

    override fun getScreen(): String {
        return Constants.editNote(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val editItem = findMenuItemById(R.id.item_edit)
        val doneItem = findMenuItemById(R.id.item_done)
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context!!, R.color.material_white),
            null, editItem, doneItem
        )

        val editing = isEditing()
        editItem?.isVisible = !editing
        doneItem?.isVisible = editing
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_edit -> {
                switchToEdit()
                return true
            }
            R.id.item_done -> {
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun hasBackPressed(): Boolean {
        if (isEditing()) {
            if (edited) {
                saveDialog()
                return true
            }
            var task: UiTask<Note>? = null
            if (saved) {
                val uiTask = getCurrentTask<UiTask<Note>>()
                val task = UiTask<Note>(
                    type = uiTask?.type ?: Type.DEFAULT,
                    state = State.EDITED,
                    action = uiTask?.action ?: Action.DEFAULT,
                    input = uiTask?.input
                )
                forResult(task, saved)
            } else {
                forResult(saved)
            }

            return true
        }
        return false
    }

    private fun initUi() {
        val uiTask = getCurrentTask<UiTask<Note>>() ?: return
        val titleRes =
            if (uiTask.action == Action.ADD) R.string.title_add_note else R.string.title_edit_note

        setTitle(titleRes)
        bind = super.binding as FragmentNoteBinding

        vm = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

        val note = uiTask.input

        note?.title?.run { noteTitle = this }
        note?.description?.run { noteDescription = this }

        bind.inputEditTitle.addTextChangedListener(object : TextChangeListener() {
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!DataUtilKt.isEquals(noteTitle, s?.toString())) {
                    edited = true
                }
                noteTitle = s.toString()
            }

        })
        bind.inputEditDescription.addTextChangedListener(object : TextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!DataUtilKt.isEquals(noteDescription, s?.toString())) {
                    edited = true
                }
                noteDescription = s.toString()
            }
        })
        resolveUi()
        if (uiTask.action == Action.EDIT || uiTask.action == Action.VIEW) {
            note?.run {
                request(id = this.id, action = Action.GET, progress = true)
            }
        }
    }

    private fun resolveUi() {
        if (isEditing()) {
            bind.layoutView.visibility = View.GONE
            bind.layoutEdit.visibility = View.VISIBLE
        } else {
            bind.layoutEdit.visibility = View.GONE
            bind.layoutView.visibility = View.VISIBLE
        }
    }

    private fun switchToEdit() {
        val uiTask = getCurrentTask<UiTask<Note>>(false)
        uiTask?.action = Action.EDIT
        val note = getInput<Note>()

        bind.inputEditTitle.setText(note?.title)
        bind.inputEditDescription.setText(note?.description)
        resolveUi()
    }

    private fun saveNote(): Boolean {
        if (noteTitle.isEmpty()) {
            bind.inputEditTitle.error = getString(R.string.error_title_note)
            return false
        }
        if (noteDescription.isEmpty()) {
            bind.inputEditDescription.error = getString(R.string.error_description_note)
            return false
        }
        edited = false
        val uiTask = getCurrentTask<UiTask<Note>>()!!
        val note = getInput<Note>()
        request(
            id = note?.id,
            state = State.DIALOG,
            action = uiTask.action,
            title = noteTitle,
            description = noteDescription,
            progress = true
        )
        return true
    }

    private fun saveDialog() {
        MaterialDialog(context!!).show {
            title(R.string.dialog_title_save_note)
            positiveButton(res = R.string.yes, click = {
                saveNote()
            })
            negativeButton(res = R.string.no, click = {
                edited = false
                forResult(saved)
            })
        }
    }

    private fun processUiState(response: Response.UiResponse) {
        Timber.v("UiState %s", response.uiState.name)
        when (response.uiState) {
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
        }
    }

    fun processSingleResponse(response: Response<NoteItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<NoteItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, item: NoteItem) {
        if (action == Action.ADD || action == Action.EDIT) {
            NotifyUtil.showInfo(getParent()!!, getString(R.string.dialog_saved_note))
            AndroidUtil.hideSoftInput(getParent()!!)
            saved = true
            //ex.postToUi(Runnable { forResult(saved) }, 500L)
            hasBackPressed()
            return
        }
        if (isEditing()) {
            bind.inputEditTitle.setText(item.item.title)
            bind.inputEditDescription.setText(item.item.description)
        } else {
            bind.textTitle.setText(item.item.title)
            bind.textDescription.setText(item.item.description)
        }
        ex.postToUi(Runnable {
            vm.updateUiState(state, action, UiState.EXTRA)
        }, 500L)
        if (state == State.DIALOG) {

        }
    }

    private fun isEditing(): Boolean {
        val uiTask = getCurrentTask<UiTask<Note>>(false)
        val action = uiTask?.action ?: Action.DEFAULT
        return action == Action.EDIT || action == Action.ADD
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        id: String? = Constants.Default.NULL,
        title: String = Constants.Default.STRING,
        description: String = Constants.Default.STRING,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val request = NoteRequest(
            state = state,
            action = action,
            id = id,
            title = title,
            description = description,
            single = true,
            progress = progress
        )
        vm.request(request)
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        id: String = Constants.Default.STRING,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val request = NoteRequest(
            state = state,
            action = action,
            id = id,
            single = true,
            progress = progress
        )
        vm.request(request)
    }
}