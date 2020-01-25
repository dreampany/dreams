package com.dreampany.tools.ui.fragment.resume

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.extension.isEmpty
import com.dreampany.framework.misc.extension.isEqual
import com.dreampany.framework.misc.extension.rawText
import com.dreampany.framework.misc.extension.toTint
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.TextChangeListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.*
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.ResumeMapper
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.databinding.ContentResumeProfileBinding
import com.dreampany.tools.databinding.FragmentResumeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.misc.ResumeRequest
import com.dreampany.tools.ui.model.ResumeItem
import com.dreampany.tools.ui.vm.resume.ResumeViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class ResumeFragment
@Inject constructor() :
    BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    @Inject
    internal lateinit var mapper: ResumeMapper
    private lateinit var bind: FragmentResumeBinding
    private lateinit var   bindProfile: ContentResumeProfileBinding

    private lateinit var vm: ResumeViewModel
    private var saved: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.fragment_resume
    }

    override fun getMenuId(): Int {
        return R.menu.menu_resume
    }

    override fun getTitleResId(): Int {
        return R.string.title_resume
    }

    override fun getScreen(): String {
        return Constants.resume(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val editItem = findMenuItemById(R.id.item_edit).toTint(context, R.color.material_white)
        val doneItem = findMenuItemById(R.id.item_done).toTint(context, R.color.material_white)

        val editing = isEditing()
        editItem?.isVisible = !editing
        doneItem?.isVisible = editing
    }

    override fun onRefresh() {
        super.onRefresh()
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_done -> {
                saveResume()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun hasBackPressed(): Boolean {
        if (isUpdated()) {
            saveDialog()
            return true
        }
        if (saved) {
            val uiTask = getCurrentTask<UiTask<Resume>>()
            val task = UiTask<Resume>(
                type = uiTask?.type ?: Type.DEFAULT,
                state = if (isResumeAdded()) State.ADDED else State.EDITED,
                action = uiTask?.action ?: Action.DEFAULT,
                input = uiTask?.input
            )
            forResult(task, saved)
        } else {
            forResult(saved)
        }
        return false
    }

    private fun initUi() {
        bind = super.binding as FragmentResumeBinding
        bindProfile = bind.contentResumeProfile
        val uiTask = getCurrentTask<UiTask<Resume>>() ?: return
        //val titleRes = if (uiTask.action == Action.ADD) R.string.title_add_note else R.string.title_edit_note

        //setTitle(titleRes)
        ViewUtil.setSwipe(bind.layoutRefresh, this)

        vm = ViewModelProvider(this, factory).get(ResumeViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

        if (uiTask.action == Action.EDIT || uiTask.action == Action.VIEW) {
            // get ui item
            uiTask.input?.run {
                request(state = State.UI, action = Action.GET, progress = true, input = this)
            }
        }

/*        uiTask.input?.let { resume ->
            bind.contentResumeProfile.editProfileName.addTextChangedListener(object :
                TextChangeListener() {
                override fun afterTextChanged(s: Editable?) {
                    if (!DataUtilKt.isEquals(resume.profile?.name, s?.toString())) {

                    }
                }
            })
        }*/

        //val note = uiTask.input

        /*       note?.title?.run { noteTitle = this }
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
               }*/
    }

    private fun isUpdated() : Boolean {
        val task: UiTask<Resume>? = getCurrentTask<UiTask<Resume>>()
        val resume : Resume? = task?.input
        if (!DataUtilKt.isEquals(resume?.profile?.name, bindProfile.editProfileName.rawText())) return true
        if (!DataUtilKt.isEquals(resume?.profile?.designation, bindProfile.editProfileDesignation.rawText())) return true
        if (!DataUtilKt.isEquals(resume?.profile?.phone, bindProfile.editProfilePhone.rawText())) return true
        if (!DataUtilKt.isEquals(resume?.profile?.email, bindProfile.editProfileEmail.rawText())) return true
        if (!DataUtilKt.isEquals(resume?.profile?.currentAddress, bindProfile.editProfileCurrentAddress.rawText())) return true
        if (!DataUtilKt.isEquals(resume?.profile?.permanentAddress, bindProfile.editProfilePermanentAddress.rawText())) return true
        return false
    }

    private fun isResumeAdded(): Boolean {
        val task: UiTask<Resume> = getCurrentTask<UiTask<Resume>>() ?: return false
        return task.action == Action.ADD && saved
    }

    private fun isResumeEdited(): Boolean {
        val task: UiTask<Resume> = getCurrentTask<UiTask<Resume>>() ?: return false
        return task.action == Action.EDIT && saved
    }

    private fun isEditing(): Boolean {
        val uiTask = getCurrentTask<UiTask<Resume>>(false)
        val action = uiTask?.action ?: Action.DEFAULT
        return action == Action.EDIT || action == Action.ADD
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

    fun processSingleResponse(response: Response<ResumeItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<ResumeItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, item: ResumeItem) {
        if (action == Action.ADD || action == Action.EDIT) {
            NotifyUtil.showInfo(getParent()!!, getString(R.string.dialog_saved_resume))
            AndroidUtil.hideSoftInput(getParent()!!)
            saved = true
            val uiTask = getCurrentTask<UiTask<Resume>>()
            uiTask?.apply {
                input = item.item
            }
            hasBackPressed()
            return
        }
        bind.contentResumeProfile.editProfileName.setText(item.item.profile?.name)
        bind.contentResumeProfile.editProfileDesignation.setText(item.item.profile?.designation)
        bind.contentResumeProfile.editProfilePhone.setText(item.item.profile?.phone)
        bind.contentResumeProfile.editProfileEmail.setText(item.item.profile?.email)
        bind.contentResumeProfile.editProfileCurrentAddress.setText(item.item.profile?.currentAddress)
        bind.contentResumeProfile.editProfilePermanentAddress.setText(item.item.profile?.permanentAddress)
        ex.postToUi(Runnable {
            vm.updateUiState(state, action, UiState.EXTRA)
        }, 500L)
        if (state == State.DIALOG) {

        }
    }

    private fun saveResume(): Boolean {
        if (bind.contentResumeProfile.editProfileName.isEmpty()) {
            bind.contentResumeProfile.editProfileName.error = getString(R.string.error_resume_name)
            return false
        }
        val uiTask = getCurrentTask<UiTask<Resume>>()
        val profile = mapper.getProfileMap(
            id = uiTask?.input?.profile?.id,
            name = bind.contentResumeProfile.editProfileName.rawText(),
            designation = bind.contentResumeProfile.editProfileDesignation.rawText(),
            phone = bind.contentResumeProfile.editProfilePhone.rawText(),
            email = bind.contentResumeProfile.editProfileEmail.rawText(),
            currentAddress = bind.contentResumeProfile.editProfileCurrentAddress.rawText(),
            permanentAddress = bind.contentResumeProfile.editProfilePermanentAddress.rawText()
        )
        uiTask?.run {
            request(
                state = State.DIALOG,
                action = uiTask.action,
                progress = true,
                input = uiTask.input,
                profile = profile
            )
        }
        return true
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        progress: Boolean = Constants.Default.BOOLEAN,
        input: Resume? = Constants.Default.NULL,
        profile: Map<String, Any>? = Constants.Default.NULL
    ) {
        val request = ResumeRequest(
            state = state,
            action = action,
            single = true,
            progress = progress,
            input = input,
            profile = profile
        )
        vm.request(request)
    }

    private fun saveDialog() {
        MaterialDialog(context!!).show {
            title(R.string.dialog_title_save_resume)
            positiveButton(res = R.string.yes, click = {
                saveResume()
            })
            negativeButton(res = R.string.no, click = {
                forResult(saved)
            })
        }
    }
}