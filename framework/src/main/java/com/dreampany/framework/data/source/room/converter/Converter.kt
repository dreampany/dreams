package com.dreampany.framework.data.source.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
open class Converter {

    protected val gson = Gson()
    private val type = object : TypeToken<List<String>>() {}.type

    @Synchronized
    @TypeConverter
    fun toString(values: List<String>?): String? {
        return if (values.isNullOrEmpty()) null
        else gson.toJson(values, type)
    }

    @Synchronized
    @TypeConverter
    fun toList(json: String?): List<String>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson<List<String>>(json, type)
    }
}