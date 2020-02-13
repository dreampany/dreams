package com.dreampany.tools.ui.fragment.resume

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.*
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.ResumeMapper
import com.dreampany.tools.data.model.resume.Profile
import com.dreampany.tools.data.model.resume.Resume
import com.dreampany.tools.databinding.*
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.resume.ExperienceAdapter
import com.dreampany.tools.ui.adapter.resume.ProjectAdapter
import com.dreampany.tools.ui.adapter.resume.SchoolAdapter
import com.dreampany.tools.ui.adapter.resume.SkillAdapter
import com.dreampany.tools.ui.misc.ResumeRequest
import com.dreampany.tools.ui.model.resume.*
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
    private lateinit var bindSkills: ContentResumeSkillsBinding
    private lateinit var bindExperiences: ContentResumeExperiencesBinding
    private lateinit var bindProjects: ContentResumeProjectsBinding
    private lateinit var bindSchools: ContentResumeSchoolsBinding

    private lateinit var skillScroller: OnVerticalScrollListener
    private lateinit var experienceScroller: OnVerticalScrollListener
    private lateinit var projectScroller: OnVerticalScrollListener
    private lateinit var schoolScroller: OnVerticalScrollListener
    private lateinit var skillAdapter: SkillAdapter
    private lateinit var experienceAdapter: ExperienceAdapter
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var schoolAdapter: SchoolAdapter
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
        //vm.updateUiState(uiState = UiState.SHOW_PROGRESS)
        val uiTask = getCurrentTask<UiTask<Resume>>() ?: return
        if (uiTask.action == Action.EDIT || uiTask.action == Action.VIEW) {
            uiTask.input?.run {
                request(state = State.UI, action = Action.GET, progress = true, input = this)
            }
        }
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
        bindExperiences = bind.contentResumeExperiences
        bindProjects = bind.contentResumeProjects
        bindSchools = bind.contentResumeSchools

        bind.layoutRefresh.bind(this)
        bindSkills.imageResumeSkillsAdd.setOnSafeClickListener {
            showSkillUi()
        }
        bindExperiences.imageResumeExperiencesAdd.setOnSafeClickListener {
            showExperienceUi()
        }

        bindProjects.imageResumeProjectsAdd.setOnSafeClickListener {
            showProjectUi()
        }

        bindSchools.imageResumeSchoolsAdd.setOnSafeClickListener {
            showSchoolUi()
        }

        vm = ViewModelProvider(this, factory).get(ResumeViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun initRecycler() {
        initSkillRecycler()
        initExperienceRecycler()
        initProjectRecycler()
        initSchoolRecycler()
    }

    private fun initSkillRecycler() {
        bind.skills = ObservableArrayList<Any>()
        skillAdapter = SkillAdapter(object : SmartAdapter.OnUiItemClickListener<SkillItem, Action>{
            override fun onUiItemClick(view: View, item: SkillItem, action: Action) {

            }

            override fun onUiItemLongClick(view: View, item: SkillItem, action: Action) {
            }

        })

        skillAdapter.setStickyHeaders(false)
        skillScroller = object : OnVerticalScrollListener() {}
        val layout = SmoothScrollLinearLayoutManager(context!!)
        layout.setAutoMeasureEnabled(true)
        bindSkills.recyclerSkill.setNestedScrollingEnabled(false)
        bindSkills.recyclerSkill.apply(
            adapter = skillAdapter,
            layout = layout,
            fixedSize = false,
            scroller = skillScroller
        )
    }

    private fun initExperienceRecycler() {
        bind.experiences = ObservableArrayList<Any>()
        experienceAdapter = ExperienceAdapter(object : SmartAdapter.OnUiItemClickListener<ExperienceItem, Action>{
            override fun onUiItemClick(view: View, item: ExperienceItem, action: Action) {

            }

            override fun onUiItemLongClick(view: View, item: ExperienceItem, action: Action) {
            }
        })
        experienceAdapter.setStickyHeaders(false)
        experienceScroller = object : OnVerticalScrollListener() {}
        val layout = SmoothScrollLinearLayoutManager(context!!)
        layout.setAutoMeasureEnabled(true)
        bindExperiences.recyclerExperiences.setNestedScrollingEnabled(false)
        //bindSkills.recylerSkill.setNestedScrollingEnabled(false)
        bindExperiences.recyclerExperiences.apply(
            adapter = experienceAdapter,
            layout = layout,
            fixedSize = false,
            scroller = experienceScroller
        )
    }

    private fun initProjectRecycler() {
        bind.projects = ObservableArrayList<Any>()
        projectAdapter = ProjectAdapter(object : SmartAdapter.OnUiItemClickListener<ProjectItem, Action>{
            override fun onUiItemClick(view: View, item: ProjectItem, action: Action) {

            }

            override fun onUiItemLongClick(view: View, item: ProjectItem, action: Action) {
            }
        })
        projectAdapter.setStickyHeaders(false)
        projectScroller = object : OnVerticalScrollListener() {}
        val layout = SmoothScrollLinearLayoutManager(context!!)
        layout.setAutoMeasureEnabled(true)
        bindProjects.recyclerProjects.setNestedScrollingEnabled(false)
        //bindSkills.recylerSkill.setNestedScrollingEnabled(false)
        bindProjects.recyclerProjects.apply(
            adapter = projectAdapter,
            layout = layout,
            fixedSize = false,
            scroller = projectScroller
        )
    }

    private fun initSchoolRecycler() {
        bind.schools = ObservableArrayList<Any>()
        schoolAdapter = SchoolAdapter(object : SmartAdapter.OnUiItemClickListener<SchoolItem, Action>{
            override fun onUiItemClick(view: View, item: SchoolItem, action: Action) {

            }

            override fun onUiItemLongClick(view: View, item: SchoolItem, action: Action) {
            }
        })
        schoolAdapter.setStickyHeaders(false)
        schoolScroller = object : OnVerticalScrollListener() {}
        val layout = SmoothScrollLinearLayoutManager(context!!)
        layout.setAutoMeasureEnabled(true)
        bindSchools.recyclerSchools.setNestedScrollingEnabled(false)
        //bindSkills.recylerSkill.setNestedScrollingEnabled(false)
        bindSchools.recyclerSchools.apply(
            adapter = schoolAdapter,
            layout = layout,
            fixedSize = false,
            scroller = schoolScroller
        )
    }

    private fun isUpdated(): Boolean {
        val task: UiTask<Resume>? = getCurrentTask<UiTask<Resume>>()
        val resume: Resume? = task?.input
        if (!DataUtilKt.isEquals(
                resume?.profile?.name,
                bindProfile.editProfileName.string()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.designation,
                bindProfile.editProfileDesignation.string()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.phone,
                bindProfile.editProfilePhone.string()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.email,
                bindProfile.editProfileEmail.string()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.currentAddress,
                bindProfile.editProfileCurrentAddress.string()
            )
        ) return true
        if (!DataUtilKt.isEquals(
                resume?.profile?.permanentAddress,
                bindProfile.editProfilePermanentAddress.string()
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
        context?.run {
            MaterialDialog(this).show {
                title(res = R.string.title_skill)
                customView(R.layout.item_resume_skill_input, scrollable = true)
                noAutoDismiss()
                cornerRadius(res = R.dimen._10sdp)
                cancelOnTouchOutside(false)
                positiveButton(R.string.save, click = { dialog ->
                    val parent = dialog.getCustomView()
                    val skillInput = parent.findViewById<TextInputEditText>(R.id.edit_skill)
                    val skill = skillInput.string()
                    if (skill.isNullOrEmpty()) {
                        skillInput.error = getString(R.string.error_resume_skill)
                        return@positiveButton
                    }
                    addSkill(skill)
                    dialog.dismiss()
                })
                negativeButton(R.string.cancel, click = { dialog ->
                    dialog.dismiss()
                })
            }
        }
    }

    private fun showExperienceUi() {
        context?.run {
            MaterialDialog(this).show {
                title(res = R.string.title_experience)
                customView(R.layout.item_resume_experience_input, scrollable = true)
                noAutoDismiss()
                cornerRadius(res = R.dimen._10sdp)
                cancelOnTouchOutside(false)
                positiveButton(R.string.save, click = { dialog ->
                    val parent = dialog.getCustomView()
                    val companyInput = parent.findViewById<TextInputEditText>(R.id.edit_experience_company)
                    val locationInput = parent.findViewById<TextInputEditText>(R.id.edit_experience_location)
                    val designationInput = parent.findViewById<TextInputEditText>(R.id.edit_experience_designation)
                    val descriptionInput = parent.findViewById<TextInputEditText>(R.id.edit_experience_description)

                    val company = companyInput.string()
                    val location = locationInput.string()
                    val designation = designationInput.string()
                    val description = descriptionInput.string()

                    if (company.isNullOrEmpty()) {
                        companyInput.error = getString(R.string.error_resume_company)
                        return@positiveButton
                    }
                    if (designation.isNullOrEmpty()) {
                        designationInput.error = getString(R.string.error_resume_designation)
                        return@positiveButton
                    }
                    addExperience(company, location, designation, description)
                    dialog.dismiss()
                })
                negativeButton(R.string.cancel, click = { dialog ->
                    dialog.dismiss()
                })
            }
        }
    }

    private fun showProjectUi() {
        context?.run {
            MaterialDialog(this).show {
                title(res = R.string.title_project)
                customView(R.layout.item_resume_project_input, scrollable = true)
                noAutoDismiss()
                cornerRadius(res = R.dimen._10sdp)
                cancelOnTouchOutside(false)
                positiveButton(R.string.save, click = { dialog ->
                    val parent = dialog.getCustomView()
                    val nameInput = parent.findViewById<TextInputEditText>(R.id.edit_project_name)
                    val descriptionInput = parent.findViewById<TextInputEditText>(R.id.edit_project_description)

                    val name = nameInput.string()
                    val description = descriptionInput.string()

                    if (name.isNullOrEmpty()) {
                        nameInput.error = getString(R.string.error_resume_name)
                        return@positiveButton
                    }

                    addProject(name, description)
                    dialog.dismiss()
                })
                negativeButton(R.string.cancel, click = { dialog ->
                    dialog.dismiss()
                })
            }
        }
    }

    private fun showSchoolUi() {
        context?.run {
            MaterialDialog(this).show {
                title(res = R.string.title_school)
                customView(R.layout.item_resume_school_input, scrollable = true)
                noAutoDismiss()
                cornerRadius(res = R.dimen._10sdp)
                cancelOnTouchOutside(false)
                positiveButton(R.string.save, click = { dialog ->
                    val parent = dialog.getCustomView()
                    val nameInput = parent.findViewById<TextInputEditText>(R.id.edit_school_name)
                    val locationInput = parent.findViewById<TextInputEditText>(R.id.edit_school_location)
                    val degreeInput = parent.findViewById<TextInputEditText>(R.id.edit_school_degree)
                    val descriptionInput = parent.findViewById<TextInputEditText>(R.id.edit_school_description)

                    val name = nameInput.string()
                    val location = locationInput.string()
                    val degree = degreeInput.string()
                    val description = descriptionInput.string()

                    if (name.isNullOrEmpty()) {
                        nameInput.error = getString(R.string.error_resume_name)
                        return@positiveButton
                    }

                    addSchool(name, location, degree, description)
                    dialog.dismiss()
                })
                negativeButton(R.string.cancel, click = { dialog ->
                    dialog.dismiss()
                })
            }
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
                Subtype.EXPERIENCE -> {
                    experienceAdapter.clear()
                    item.experiences.run {
                        experienceAdapter.addItems(this)
                    }
                    return
                }
                Subtype.PROJECT -> {
                    projectAdapter.clear()
                    item.projects.run {
                        projectAdapter.addItems(this)
                    }
                    return
                }
                Subtype.SCHOOL -> {
                    schoolAdapter.clear()
                    item.schools.run {
                        schoolAdapter.addItems(this)
                    }
                    return
                }
            }

            //added resume by done menu
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
        updateProfile(item.item.profile)
        skillAdapter.clear()
        item.skills.run {
            skillAdapter.addItems(this)
        }
        experienceAdapter.clear()
        item.experiences.run {
            experienceAdapter.addItems(this)
        }
        projectAdapter.clear()
        item.projects.run {
            projectAdapter.addItems(this)
        }
        schoolAdapter.clear()
        item.schools.run {
            schoolAdapter.addItems(this)
        }
    }

    private fun updateProfile(profile: Profile?) {
        bindProfile.editProfileName.setText(profile?.name)
        bindProfile.editProfileDesignation.setText(profile?.designation)
        bindProfile.editProfilePhone.setText(profile?.phone)
        bindProfile.editProfileEmail.setText(profile?.email)
        bindProfile.editProfileCurrentAddress.setText(profile?.currentAddress)
        bindProfile.editProfilePermanentAddress.setText(profile?.permanentAddress)
    }

    private fun saveResume(): Boolean {
        if (bindProfile.editProfileName.isEmpty()) {
            bindProfile.editProfileName.error = getString(R.string.error_resume_name)
            return false
        }
        val uiTask = getCurrentTask<UiTask<Resume>>()
        val profile = mapper.getProfileMap(
            id = uiTask?.input?.profile?.id,
            name = bindProfile.editProfileName.string(),
            designation = bindProfile.editProfileDesignation.string(),
            phone = bindProfile.editProfilePhone.string(),
            email = bindProfile.editProfileEmail.string(),
            currentAddress = bindProfile.editProfileCurrentAddress.string(),
            permanentAddress = bindProfile.editProfilePermanentAddress.string()
        )
        val skills = arrayListOf<Map<String, Any>>()
        val experiences = arrayListOf<Map<String, Any>>()
        val projects = arrayListOf<Map<String, Any>>()
        val schools = arrayListOf<Map<String, Any>>()

        skillAdapter.currentItems.forEach {
            it.item.run {
                mapper.getSkillMap(id = id, title = title)?.run {
                    skills.add(this)
                }
            }
        }
        experienceAdapter.currentItems.forEach {
            it.item.run {
                mapper.getExperienceMap(
                    id = id,
                    company = company,
                    location = location,
                    designation = designation,
                    description = description
                    )?.run {
                    experiences.add(this)
                }
            }
        }
        projectAdapter.currentItems.forEach {
            it.item.run {
                mapper.getProjectMap(
                    id = id,
                    name = name,
                    description = description
                )?.run {
                    projects.add(this)
                }
            }
        }
        schoolAdapter.currentItems.forEach {
            it.item.run {
                mapper.getSchoolMap(
                    id = id,
                    name = name,
                    location = location,
                    degree = degree,
                    description = description
                )?.run {
                    schools.add(this)
                }
            }
        }
        uiTask?.run {
            request(
                state = State.DIALOG,
                action = action,
                progress = true,
                input = input,
                profile = profile,
                skills = skills,
                experiences = experiences,
                projects = projects,
                schools = schools
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

    private fun addSkill(skill: String?) {
        if (skill.isNullOrEmpty()) return
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

    private fun addExperience(
        company: String?,
        location: String?,
        designation: String?,
        description: String?
    ) {
        if (company.isNullOrEmpty() || designation.isNullOrEmpty()) return
        val uiTask = getCurrentTask<UiTask<Resume>>()
        val experienceMap = mapper.getExperienceMap(
            company = company,
            location = location,
            designation = designation,
            description = description
        )

        val experiences = arrayListOf<Map<String, Any>>()
        experienceMap?.run {
            experiences.add(this)
        }
        uiTask?.run {
            request(
                type = Type.RESUME,
                subtype = Subtype.EXPERIENCE,
                action = Action.ADD,
                progress = true,
                input = input,
                experiences = experiences
            )
        }

    }

    private fun addProject(name: String?, description: String?) {
        if (name.isNullOrEmpty()) return
        val uiTask = getCurrentTask<UiTask<Resume>>()
        val projectMap = mapper.getProjectMap(name = name, description = description)

        val projects = arrayListOf<Map<String, Any>>()
        projectMap?.run {
            projects.add(this)
        }
        uiTask?.run {
            request(
                type = Type.RESUME,
                subtype = Subtype.PROJECT,
                action = Action.ADD,
                progress = true,
                input = input,
                projects = projects
            )
        }

    }

    private fun addSchool(name: String?, location: String?, degree: String?, description: String?) {
        if (name.isNullOrEmpty()) return
        val uiTask = getCurrentTask<UiTask<Resume>>()
        val schoolMap = mapper.getSchoolMap(
            name = name,
            location = location,
            degree = degree,
            description = description
        )

        val schools = arrayListOf<Map<String, Any>>()
        schoolMap?.run {
            schools.add(this)
        }
        uiTask?.run {
            request(
                type = Type.RESUME,
                subtype = Subtype.SCHOOL,
                action = Action.ADD,
                progress = true,
                input = input,
                schools = schools
            )
        }

    }
}