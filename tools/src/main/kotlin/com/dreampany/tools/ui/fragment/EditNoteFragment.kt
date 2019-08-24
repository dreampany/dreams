package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.ui.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.NotifyUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.misc.NoteRequest
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.databinding.FragmentEditNoteBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.NoteItem
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.tools.vm.NoteViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Roman-372 on 8/6/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class EditNoteFragment
@Inject constructor() :
        BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentEditNoteBinding

    private lateinit var vm: NoteViewModel
    private var edited: Boolean = false
    private var saved: Boolean = false
    private var noteTitle: String = Constants.Default.STRING
    private var noteDescription: String = Constants.Default.STRING

    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_note
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
    }

    override fun hasBackPressed(): Boolean {
        if (edited) {
            saveDialog()
            return true
        }
        forResult(saved)
        return true
    }

    private fun initUi() {
        val uiTask = getCurrentTask<UiTask<Note>>() ?: return
        val titleRes =
                if (uiTask.action == Action.ADD) R.string.title_add_note else R.string.title_edit_note

        setTitle(titleRes)
        bind = super.binding as FragmentEditNoteBinding

        vm = ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

        val note = getInput<Note>()
        note?.title?.run { noteTitle = this }
        note?.description?.run { noteDescription = this }
        bind.inputEditTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!noteTitle.equals(s)) {
                    edited = true
                }
                noteTitle = s.toString()
            }

        })
        bind.inputEditDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!noteDescription.equals(s)) {
                    edited = true
                }
                noteDescription = s.toString()
            }

        })
        if (uiTask.action == Action.EDIT) {
            request(action = Action.GET, id = note!!.id, progress = true)
        }
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
                action = if (uiTask.action == Action.EDIT) Action.UPDATE else Action.ADD,
                id = note?.id,
                title = noteTitle,
                description = noteDescription,
                progress = true
        )
        return true
    }

    private fun request(
            action: Action = Action.DEFAULT,
            id: String? = Constants.Default.NULL,
            title: String = Constants.Default.STRING,
            description: String = Constants.Default.STRING,
            progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val request = NoteRequest(
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
            action: Action = Action.DEFAULT,
            id: String = Constants.Default.STRING,
            progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val request = NoteRequest(
                action = action,
                id = id,
                single = true,
                progress = progress
        )
        vm.request(request)
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

    private fun processUiState(state: UiState) {
        Timber.v("UiState %s", state.name)
        when (state) {
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
            Timber.v("processSingleResponse %s", result.loading)
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<NoteItem>
            processSuccess(result.action, result.data)
        }
    }

    private fun processSuccess(action: Action, item: NoteItem) {
        saved = true
        if (action == Action.UPDATE) {
            NotifyUtil.showInfo(getParent()!!, getString(R.string.dialog_saved_note))
            AndroidUtil.hideSoftInput(getParent()!!)
            ex.postToUi(Runnable { forResult() }, 500L)
            return
        }
        bind.inputEditTitle.setText(item.item.title)
        bind.inputEditDescription.setText(item.item.description)
        ex.postToUi(Runnable { processUiState(UiState.EXTRA) }, 500L)
    }
}