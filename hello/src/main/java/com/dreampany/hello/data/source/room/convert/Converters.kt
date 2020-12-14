package com.dreampany.hello.data.source.room.convert

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converter.Converter
import com.dreampany.hello.data.enums.Gender
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {

    private val mapType = object : TypeToken<Map<String, String>>() {}.type

    @Synchronized
    @TypeConverter
    fun toString(input: Gender?): String? {
        return if (input == null) null else input.name
    }

    @Synchronized
    @TypeConverter
    fun toGender(input: String?): Gender? {
        return if (input.isNullOrEmpty()) null else Gender.valueOf(input)
    }

    @Synchronized
    @TypeConverter
    fun toString(values: Map<String, String>?): String? {
        return if (values.isNullOrEmpty()) null
        else gson.toJson(values, mapType)
    }

    @Synchronized
    @TypeConverter
    fun toMap(json: String?): Map<String, String>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson<Map<String, String>>(json, mapType)
    }
}