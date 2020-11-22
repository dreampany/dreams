package com.dreampany.tools.data.source.crypto.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converter.Converter
import com.dreampany.tools.data.enums.crypto.Category
import com.dreampany.tools.data.model.crypto.Currency
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 3/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {

    private val urlsType = object : TypeToken<Map<String, List<String>>>() {}.type

    @Synchronized
    @TypeConverter
    fun toString(input: Currency.Type?): String? {
        return if (input == null) null else input.name
    }

    @Synchronized
    @TypeConverter
    fun toCurrencyType(input: String?): Currency.Type? {
        return if (input.isNullOrEmpty()) null else Currency.Type.valueOf(input)
    }

    @Synchronized
    @TypeConverter
    fun toString(input: Category?): String? {
        return if (input == null) null else input.name
    }

    @Synchronized
    @TypeConverter
    fun toCategory(input: String?): Category? {
        return if (input.isNullOrEmpty()) null else Category.valueOf(input)
    }

    @Synchronized
    @TypeConverter
    fun toString(values: Map<String, List<String>>?): String? {
        return if (values.isNullOrEmpty()) null
        else gson.toJson(values, urlsType)
    }

    @Synchronized
    @TypeConverter
    fun toUrls(json: String?): Map<String, List<String>>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson<Map<String, List<String>>>(json, urlsType)
    }
}