package com.dreampany.tube.data.source.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converter.Converter
import com.dreampany.tube.data.model.Page
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 3/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {

    private val mapType = object : TypeToken<Map<String, Long>>() {}.type

/*    @Synchronized
    @TypeConverter
    fun toString(input: Category.Type?): String? = if (input == null) null else input.name

    @Synchronized
    @TypeConverter
    fun toCategoryType(input: String?): Category.Type? =
        if (input.isNullOrEmpty()) null else Category.Type.valueOf(input)*/

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

    @Synchronized
    @TypeConverter
    fun toString(input: Page.Type?): String? = if (input == null) null else input.name

    @Synchronized
    @TypeConverter
    fun toPageType(input: String?): Page.Type? =
        if (input.isNullOrEmpty()) null else Page.Type.valueOf(input)
}