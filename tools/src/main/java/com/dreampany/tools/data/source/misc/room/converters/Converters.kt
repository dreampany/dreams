package com.dreampany.tools.data.source.misc.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converter.Converter
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 3/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {

    private val mapType = object : TypeToken<Map<String, Long>>() {}.type

    @Synchronized
    @TypeConverter
    fun toString(values: Map<String, Long>?): String? {
        return if (values.isNullOrEmpty()) null
        else gson.toJson(values, mapType)
    }

    @Synchronized
    @TypeConverter
    fun toMap(json: String?): Map<String, Long>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson<Map<String, Long>>(json, mapType)
    }
}