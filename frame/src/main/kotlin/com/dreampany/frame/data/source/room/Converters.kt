package com.dreampany.frame.data.source.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 2019-08-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
open class Converters {
    protected val gson = Gson()
    private val type = object : TypeToken<ArrayList<String>>() {}.type

    @TypeConverter
    @Synchronized
    fun toString(values: ArrayList<String>?): String? {
        return if (values.isNullOrEmpty()) {
            null
        } else gson.toJson(values, type)
    }

    @TypeConverter
    @Synchronized
    fun toList(json: String?): ArrayList<String>? {
        return if (json.isNullOrEmpty()) { null } else gson.fromJson<ArrayList<String>>(json, type)
    }
}