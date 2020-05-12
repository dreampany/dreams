package com.dreampany.tools.data.source.history.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.model.Link
import com.dreampany.framework.data.source.room.converter.Converter
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistoryState
import com.dreampany.tools.data.enums.history.HistorySubtype
import com.dreampany.tools.data.enums.history.HistoryType
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 3/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {
    private val linkType = object : TypeToken<List<Link>>() {}.type

    @Synchronized
    @TypeConverter
    fun toString(input: HistorySource?): String? {
        return if (input == null) null else input.value
    }

    @Synchronized
    @TypeConverter
    fun toSource(input: String?): HistorySource? {
        return if (input.isNullOrEmpty()) null else HistorySource.valueOf(input)
    }

    @Synchronized
    @TypeConverter
    fun toString(input: HistoryType?): String? {
        return if (input == null) null else input.value
    }

    @Synchronized
    @TypeConverter
    fun toType(input: String?): HistoryType? {
        return if (input.isNullOrEmpty()) null else HistoryType.valueOf(input)
    }

    @Synchronized
    @TypeConverter
    fun toString(input: HistorySubtype?): String? {
        return if (input == null) null else input.value
    }

    @Synchronized
    @TypeConverter
    fun toSubtype(input: String?): HistorySubtype? {
        return if (input.isNullOrEmpty()) null else HistorySubtype.valueOf(input)
    }

    @Synchronized
    @TypeConverter
    fun toString(input: HistoryState?): String? {
        return if (input == null) null else input.value
    }

    @Synchronized
    @TypeConverter
    fun toState(input: String?): HistoryState? {
        return if (input.isNullOrEmpty()) null else HistoryState.valueOf(input)
    }

    @Synchronized
    @TypeConverter
    fun toLinkString(inputs: List<Link>?): String? {
        return if (inputs.isNullOrEmpty()) null
        else gson.toJson(inputs, linkType)
    }

    @Synchronized
    @TypeConverter
    fun toLinkList(json: String?): List<Link>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson<List<Link>>(json, linkType)
    }
}