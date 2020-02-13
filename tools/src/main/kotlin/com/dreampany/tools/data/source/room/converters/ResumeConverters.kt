package com.dreampany.tools.data.source.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converters.Converters
import com.dreampany.tools.data.model.resume.Experience
import com.dreampany.tools.data.model.resume.Project
import com.dreampany.tools.data.model.resume.School
import com.dreampany.tools.data.model.resume.Skill
import com.google.gson.reflect.TypeToken

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResumeConverters : Converters() {
    private val skillType = object : TypeToken<ArrayList<Skill>>() {}.type
    private val expType = object : TypeToken<ArrayList<Experience>>() {}.type
    private val projectType = object : TypeToken<ArrayList<Project>>() {}.type
    private val schoolType = object : TypeToken<ArrayList<School>>() {}.type


    @TypeConverter
    @Synchronized
    fun toSkillString(exp: ArrayList<Skill>?): String? {
        return if (exp.isNullOrEmpty()) null
        else gson.toJson(exp, skillType)
    }

    @TypeConverter
    @Synchronized
    fun toSkillList(json: String?): ArrayList<Skill>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson(json, skillType)
    }

    @TypeConverter
    @Synchronized
    fun toExpString(exp: ArrayList<Experience>?): String? {
        return if (exp.isNullOrEmpty()) null
        else gson.toJson(exp, expType)
    }

    @TypeConverter
    @Synchronized
    fun toExpList(json: String?): ArrayList<Experience>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson(json, expType)
    }

    @TypeConverter
    @Synchronized
    fun toProjectString(exp: ArrayList<Project>?): String? {
        return if (exp.isNullOrEmpty()) null
        else gson.toJson(exp, projectType)
    }

    @TypeConverter
    @Synchronized
    fun toProjectList(json: String?): ArrayList<Project>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson(json, projectType)
    }

    @TypeConverter
    @Synchronized
    fun toSchoolString(exp: ArrayList<School>?): String? {
        return if (exp.isNullOrEmpty()) null
        else gson.toJson(exp, schoolType)
    }

    @TypeConverter
    @Synchronized
    fun toSchoolList(json: String?): ArrayList<School>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson(json, schoolType)
    }
}