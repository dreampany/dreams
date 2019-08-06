package com.dreampany.tools.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.misc.NoteRequest
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.databinding.FragmentEditNoteBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.enums.UiSubtype
import com.dreampany.tools.ui.model.NoteItem
import com.dreampany.tools.ui.model.UiTask
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
class EditNoteFragment @Inject constructor() :
    BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentEditNoteBinding

    private lateinit var vm: NoteViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_note
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    override fun hasBackPressed(): Boolean {
        saveNote()
        return false
    }

    private fun initUi() {
        val uiTask = getCurrentTask<UiTask<Note>>() ?: return
        val titleRes =
            if (uiTask.subtype == UiSubtype.ADD) R.string.title_add_note else R.string.title_edit_note

        setTitle(titleRes)
        bind = super.binding as FragmentEditNoteBinding

        vm = ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun saveNote() {
        val title = bind.inputEditTitle.text
        if (title.isNullOrEmpty()) {
            bind.inputEditTitle.error = getString(R.string.error_title_note)
            return
        }
        val description = bind.inputEditDescription.text
        if (description.isNullOrEmpty()) {
            bind.inputEditDescription.error = getString(R.string.error_description_note)
            return
        }
        request(action = Action.ADD, title = title.toString(), description = description.toString(), progress = true)
    }

    private fun request(
        action: Action = Action.DEFAULT,
        title: String = Constants.Default.STRING,
        description: String = Constants.Default.STRING,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val request = NoteRequest(
            action = action,
            title = title,
            description = description,
            single = true,
            progress = progress
        )
        vm.request(request)
    }

    private fun processUiState(state: UiState) {
        Timber.v("UiState %s", state.name)
/*        when (state) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
                initTitleSubtitle()
            }
        }*/
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

    private fun processSuccess(action: Action, items: NoteItem) {

        ex.postToUi({ processUiState(UiState.EXTRA) }, 500L)
    }
}