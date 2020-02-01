package com.dreampany.tools.ui.fragment.resume

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.extension.*
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.*
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.ResumeMapper
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.databinding.ContentResumeProfileBinding
import com.dreampany.tools.databinding.ContentResumeSkillsBinding
import com.dreampany.tools.databinding.FragmentResumeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.resume.ResumeAdapter
import com.dreampany.tools.ui.adapter.resume.SkillAdapter
import com.dreampany.tools.ui.misc.ResumeRequest
import com.dreampany.tools.ui.model.resume.ResumeItem
import com.dreampany.tools.ui.vm.resume.ResumeViewModel
import com.google.android.material.textfield.TextInputEditText
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
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
    private lateinit var bindProfile: ContentResumeProfileBinding
    private lateinit var bindSkills : ContentResumeSkillsBinding

    private lateinit var skillScroller: OnVerticalScrollListener
    private lateinit var skillAdapter: SkillAdapter
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
        return Constants.resume(context)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
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
        bindSkills = bind.contentResumeSkills

        val uiTask = getCurrentTask<UiTask<Resume>>() ?: return

        bind.layoutRefresh.bind(this)
        bindSkills.imageResumeSkillsAdd.setOnSafeClickListener {
            showSkillUi()
        }
        bind.contentResumeExperiences.imageResumeExperiencesAdd.setOnSafeClickListener {

        }
        bind.contentResumeProjects.imageResumeProjectsAdd.setOnSafeClickListener {

        }
        bind.contentResumeSchools.imageResumeSchoolsAdd.setOnSafeClickListener {

        }

        vm = ViewModelProvider(this, factory).get(ResumeViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

        if (uiTask.action == Action.EDIT || uiTask.action == Action.VIEW) {
            // get ui item
            uiTask.input?.run {
                request(state = State.UI, action = Action.GET, progress = true, input = this)
            }
        }
    }

    private fun initRecycler() {
        initSkillRecycler()
        initExperienceRecycler()
        initProjectRecycler()
        initSchoolRecycler()
    }

    private fun initSkillRecycler() {
        bind.skills = ObservableArrayList<Any>()
        skillAdapter = SkillAdapter(this)
        skillAdapter.setStickyHeaders(false)
        skillScroller = object : OnVerticalScrollListener() {}
        bindSkills.recylerSkill.apply(adapter = skillAdapter, layout = SmoothScrollLinearLayoutManager(context!!), scroller = skillScroller)
     }

    private fun initExperienceRecycler() {
        //bind.experiences = ObservableArrayList<Any>()
    }

    private fun initProjectRecycler() {
        //bind.projects = ObservableArrayList<Any>()
    }
    private fun initSchoolRecycler() {
        //bind.schools = ObservableArrayList<Any>()
    }

    private fun isUpdated(): Boolean {
        val task: UiTask<Resume>? = getCurrentTask<UiTask<Resume>>()
        val resume: Resume? = task?.input
        if (!DataUtilKt.isEquals(
                resume?.profile?.name,
                bindProfile.editProfileName.rawText()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.designation,
                bindProfile.editProfileDesignation.rawText()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.phone,
                bindProfile.editProfilePhone.rawText()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.email,
                bindProfile.editProfileEmail.rawText()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.currentAddress,
                bindProfile.editProfileCurrentAddress.rawText()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.permanentAddress,
                bindProfile.editProfilePermanentAddress.rawText()
            )
        ) return true
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

    private fun showSkillUi() {
        MaterialDialog(context!!).show {
            title(res = R.string.title_skill)
            customView(R.layout.item_resume_skill_input, scrollable = true)
            noAutoDismiss()
            cornerRadius(res = R.dimen._10sdp)
            cancelOnTouchOutside(false)
            positiveButton(R.string.save, click = { dialog ->
                val input = dialog.getCustomView().findViewById<TextInputEditText>(R.id.edit_skill)
                val skill = input.rawText()
                Timber.v("Skill %s", skill)
                skill?.run {
                    addSkill(this)
                }
                dialog.dismiss()
            })
            negativeButton(R.string.cancel, click = { dialog ->
                dialog.dismiss()
            })
        }
    }

    private fun showExperienceUi() {

    }

    private fun showProjectUi() {

    }

    private fun showSchoolUi() {

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
            vm.processProgress(
                result.type,
                result.subtype,
                result.state,
                result.action,
                result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(
                result.type,
                result.subtype,
                result.state,
                result.action,
                result.error
            )
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<ResumeItem>
            processSuccess(result.type, result.subtype, result.state, result.action, result.data)
        }
    }

    private fun processSuccess(
        type: Type,
        subtype: Subtype,
        state: State,
        action: Action,
        item: ResumeItem
    ) {
        if (action == Action.ADD || action == Action.EDIT) {
            when (subtype) {
                Subtype.SKILL -> {
                    skillAdapter.clear()
                    item.skills.run {
                        skillAdapter.addItems(this)
                    }
                    return
                }
            }
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
            vm.updateUiState(type, subtype, state, action, UiState.EXTRA)
        }, 500L)
        if (state == State.DIALOG) {

        }
    }

    private fun saveResume(): Boolean {
        if (bindProfile.editProfileName.isEmpty()) {
            bindProfile.editProfileName.error = getString(R.string.error_resume_name)
            return false
        }
        val uiTask = getCurrentTask<UiTask<Resume>>()
        val profile = mapper.getProfileMap(
            id = uiTask?.input?.profile?.id,
            name = bindProfile.editProfileName.rawText(),
            designation = bindProfile.editProfileDesignation.rawText(),
            phone = bindProfile.editProfilePhone.rawText(),
            email = bindProfile.editProfileEmail.rawText(),
            currentAddress = bindProfile.editProfileCurrentAddress.rawText(),
            permanentAddress = bindProfile.editProfilePermanentAddress.rawText()
        )
        uiTask?.run {
            request(
                state = State.DIALOG,
                action = action,
                progress = true,
                input = input,
                profile = profile
            )
        }
        return true
    }

    private fun request(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        progress: Boolean = Constants.Default.BOOLEAN,
        input: Resume? = Constants.Default.NULL,
        profile: Map<String, Any>? = Constants.Default.NULL,
        skills: List<Map<String, Any>>? = Constants.Default.NULL,
        experiences: List<Map<String, Any>>? = Constants.Default.NULL,
        projects: List<Map<String, Any>>? = Constants.Default.NULL,
        schools: List<Map<String, Any>>? = Constants.Default.NULL
    ) {
        val request = ResumeRequest(
            type = type,
            subtype = subtype,
            state = state,
            action = action,
            single = true,
            progress = progress,
            input = input,
            profile = profile,
            skills = skills,
            experiences = experiences,
            projects = projects,
            schools = schools
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

    private fun addSkill(skill: String) {
        val uiTask = getCurrentTask<UiTask<Resume>>()
        val skillMap = mapper.getSkillMap(title = skill)

        val skills = arrayListOf<Map<String, Any>>()
        skillMap?.run {
            skills.add(this)
        }
        uiTask?.run {
            request(
                type = Type.RESUME,
                subtype = Subtype.SKILL,
                action = Action.ADD,
                progress = true,
                input = input,
                skills = skills
            )
        }
    }
}