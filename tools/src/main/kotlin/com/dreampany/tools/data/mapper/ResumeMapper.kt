package com.dreampany.tools.data.mapper

import com.dreampany.framework.data.misc.Mapper
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.misc.extension.hash
import com.dreampany.framework.misc.extension.hash512
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.model.*
import com.dreampany.tools.injector.annote.ProfileAnnote
import com.dreampany.tools.injector.annote.ProfileItemAnnote
import com.dreampany.tools.injector.annote.ResumeAnnote
import com.dreampany.tools.injector.annote.ResumeItemAnnote
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.ProfileItem
import com.dreampany.tools.ui.model.resume.ResumeItem
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ResumeMapper
@Inject constructor(
    @ResumeAnnote private val map: SmartMap<String, Resume>,
    @ResumeAnnote private val cache: SmartCache<String, Resume>,
    @ResumeItemAnnote private val uiMap: SmartMap<String, ResumeItem>,
    @ResumeItemAnnote private val uiCache: SmartCache<String, ResumeItem>,
    @ProfileAnnote private val profileMap: SmartMap<String, Profile>,
    @ProfileAnnote private val ProfileCache: SmartCache<String, Profile>,
    @ProfileItemAnnote private val profileUiMap: SmartMap<String, ProfileItem>,
    @ProfileItemAnnote private val profileUiCache: SmartCache<String, ProfileItem>
) : Mapper() {

    fun putUiItem(id: String, uiItem: ResumeItem) {
        uiMap.put(id, uiItem)
    }

    fun getUiItem(id: String): ResumeItem? {
        return uiMap.get(id)
    }

    fun getProfileMap(
        id: String?,
        name: String?,
        designation: String?,
        phone: String?,
        email: String?,
        currentAddress: String?,
        permanentAddress: String?
    ): Map<String, Any>? {
        if (name.isNullOrEmpty()) return null
        val id = id ?: id.hash()
        val profile: HashMap<String, Any> = Maps.newHashMap<String, Any>()
        profile.apply {
            put(Constants.Keys.Profile.ID, id)
            put(Constants.Keys.Profile.TIME, TimeUtilKt.currentMillis())
            put(Constants.Keys.Profile.NAME, name)
            designation?.run {
                put(Constants.Keys.Profile.DESIGNATION, this)
            }
            phone?.run {
                put(Constants.Keys.Profile.PHONE, this)
            }
            email?.run {
                put(Constants.Keys.Profile.EMAIL, this)
            }
            currentAddress?.run {
                put(Constants.Keys.Profile.CURRENT_ADDRESS, this)
            }
            permanentAddress?.run {
                put(Constants.Keys.Profile.PERMANENT_ADDRESS, this)
            }
        }
        return profile
    }

    fun getSkillMap(
        id: String? = null,
        title: String?
    ): Map<String, Any>? {
        if (title.isNullOrEmpty()) return null
        val id = id ?: title.hash512()
        val skill: HashMap<String, Any> = Maps.newHashMap<String, Any>()
        skill.apply {
            put(Constants.Keys.Skill.ID, id)
            put(Constants.Keys.Skill.TIME, TimeUtilKt.currentMillis())
            put(Constants.Keys.Skill.TITLE, title)
        }
        return skill
    }

    fun getItem(
        id: String?,
        profile: Map<String, Any>?,
        skills: List<Map<String, Any>>?,
        experiences: List<Map<String, Any>>?,
        projects: List<Map<String, Any>>?,
        schools: List<Map<String, Any>>?
    ): Resume? {
        val id = id ?: id.hash()
        var resume = map.get(id)
        if (resume == null) {
            resume = Resume(id)
            map.put(id, resume)
        }
        applyProfile(resume, profile)
        applySkills(resume, skills)
        applyExperiences(resume, experiences)
        applyProjects(resume, projects)
        applySchools(resume, schools)
        return resume
    }

    fun applyProfile(
        resume: Resume,
        profileData: Map<String, Any>?
    ) {
        if (profileData.isNullOrEmpty()
        ) {
            return
        }
        val time = profileData.get(Constants.Keys.Profile.TIME) as Long
        val id = profileData.get(Constants.Keys.Profile.ID) as String
        val profile = Profile(time = time, id = id)
        profileData.get(Constants.Keys.Profile.NAME)?.run {
            profile.name = this as String
        }
        profileData.get(Constants.Keys.Profile.DESIGNATION)?.run {
            profile.designation = this as String
        }
        profileData.get(Constants.Keys.Profile.PHONE)?.run {
            profile.phone = this as String
        }
        profileData.get(Constants.Keys.Profile.EMAIL)?.run {
            profile.email = this as String
        }
        profileData.get(Constants.Keys.Profile.CURRENT_ADDRESS)?.run {
            profile.currentAddress = this as String
        }
        profileData.get(Constants.Keys.Profile.PERMANENT_ADDRESS)?.run {
            profile.permanentAddress = this as String
        }
        resume.profile = profile
    }

    fun applySkills(
        resume: Resume,
        skillsData: List<Map<String, Any>>?
    ) {
        if (skillsData.isNullOrEmpty()
        ) {
            return
        }
        if (resume.skills.isNullOrEmpty()) {
            resume.skills = ArrayList()
        }
        resume.skills?.clear()
        skillsData.forEach {
            getSkill(it)?.let { skill ->
                resume.skills?.run {
                    if (!contains(skill))
                        add(skill)
                }

            }
        }
    }

    fun applyExperiences(
        resume: Resume,
        experiencesData: List<Map<String, Any>>?
    ) {
        if (experiencesData.isNullOrEmpty()
        ) {
            return
        }
        if (resume.experiences.isNullOrEmpty()) {
            resume.experiences = ArrayList()
        }
        resume.experiences?.clear()
        experiencesData.forEach {
            getExperience(it)?.run {
                resume.experiences?.add(this)
            }
        }
    }

    fun applyProjects(
        resume: Resume,
        projectsData: List<Map<String, Any>>?
    ) {
        if (projectsData.isNullOrEmpty()
        ) {
            return
        }
        if (resume.projects.isNullOrEmpty()) {
            resume.projects = ArrayList()
        }
        resume.projects?.clear()
        projectsData.forEach {
            getProject(it)?.run {
                resume.projects?.add(this)
            }
        }
    }

    fun applySchools(
        resume: Resume,
        schoolsData: List<Map<String, Any>>?
    ) {
        if (schoolsData.isNullOrEmpty()
        ) {
            return
        }
        if (resume.schools.isNullOrEmpty()) {
            resume.schools = ArrayList()
        }
        resume.schools?.clear()
        schoolsData.forEach {
            getSchool(it)?.run {
                resume.schools?.add(this)
            }
        }
    }

    fun getSkill(
        skillData: Map<String, Any>?
    ): Skill? {
        if (skillData.isNullOrEmpty()
        ) {
            return null
        }
        val time = skillData.get(Constants.Keys.Skill.TIME) as Long
        val id = skillData.get(Constants.Keys.Skill.ID) as String
        val skill = Skill(time = time, id = id)
        skillData.get(Constants.Keys.Skill.TITLE)?.run {
            skill.title = this as String
        }
        return skill
    }

    fun getExperience(
        experienceData: Map<String, Any>?
    ): Experience? {
        if (experienceData.isNullOrEmpty()
        ) {
            return null
        }
        val time = experienceData.get(Constants.Keys.Experience.TIME) as Long
        val id = experienceData.get(Constants.Keys.Experience.ID) as String
        val experience = Experience(time = time, id = id)
        experienceData.get(Constants.Keys.Experience.COMPANY)?.run {
            experience.company = this as String
        }
        experienceData.get(Constants.Keys.Experience.LOCATION)?.run {
            experience.location = this as String
        }
        experienceData.get(Constants.Keys.Experience.DESIGNATION)?.run {
            experience.designation = this as String
        }
        experienceData.get(Constants.Keys.Experience.DESCRIPTION)?.run {
            experience.description = this as String
        }
        experienceData.get(Constants.Keys.Experience.FROM)?.run {
            experience.from = this as Long
        }
        experienceData.get(Constants.Keys.Experience.TO)?.run {
            experience.to = this as Long
        }
        return experience
    }

    fun getProject(
        projectData: Map<String, Any>?
    ): Project? {
        if (projectData.isNullOrEmpty()
        ) {
            return null
        }
        val time = projectData.get(Constants.Keys.Project.TIME) as Long
        val id = projectData.get(Constants.Keys.Project.ID) as String
        val project = Project(time = time, id = id)
        projectData.get(Constants.Keys.Project.NAME)?.run {
            project.name = this as String
        }
        projectData.get(Constants.Keys.Project.DESCRIPTION)?.run {
            project.description = this as String
        }
        projectData.get(Constants.Keys.Project.FROM)?.run {
            project.from = this as Long
        }
        projectData.get(Constants.Keys.Project.TO)?.run {
            project.to = this as Long
        }
        return project
    }

    fun getSchool(
        schoolData: Map<String, Any>?
    ): School? {
        if (schoolData.isNullOrEmpty()
        ) {
            return null
        }
        val time = schoolData.get(Constants.Keys.School.TIME) as Long
        val id = schoolData.get(Constants.Keys.School.ID) as String
        val school = School(time = time, id = id)
        schoolData.get(Constants.Keys.School.NAME)?.run {
            school.name = this as String
        }
        schoolData.get(Constants.Keys.School.LOCATION)?.run {
            school.location = this as String
        }
        schoolData.get(Constants.Keys.School.DEGREE)?.run {
            school.degree = this as String
        }
        schoolData.get(Constants.Keys.School.DESCRIPTION)?.run {
            school.description = this as String
        }
        return school
    }
}