package com.dreampany.framework.data.source.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.enums.*
import com.dreampany.language.Language
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

    @Synchronized
    @TypeConverter
    fun toTypeValue(type: Type?): String? {
        return if (type == null) null else type.name
    }

    @Synchronized
    @TypeConverter
    fun toType(value: String?): Type? {
        return if (value.isNullOrEmpty()) null else Type.valueOf(value)
    }

    @Synchronized
    @TypeConverter
    fun toSubtypeValue(subtype: Subtype?): String? {
        return if (subtype == null) null else subtype.name
    }

    @Synchronized
    @TypeConverter
    fun toSubtype(value: String?): Subtype? {
        return if (value.isNullOrEmpty()) null else Subtype.valueOf(value)
    }

    @Synchronized
    @TypeConverter
    fun toStateValue(state: State?): String? {
        return if (state == null) null else state.name
    }

    @Synchronized
    @TypeConverter
    fun toState(value: String?): State? {
        return if (value.isNullOrEmpty()) null else State.valueOf(value)
    }

    @Synchronized
    @TypeConverter
    fun toString(values: ArrayList<String>?): String? {
        return if (values.isNullOrEmpty()) {
            null
        } else gson.toJson(values, type)
    }

    @Synchronized
    @TypeConverter
    fun toList(json: String?): ArrayList<String>? {
        return if (json.isNullOrEmpty()) {
            null
        } else gson.fromJson<ArrayList<String>>(json, type)
    }

    @Synchronized
    @TypeConverter
    fun toLanguageValue(language: Language?): String? {
        return if (language == null) null else language.name
    }

    @Synchronized
    @TypeConverter
    fun toLanguage(value: String?): Language? {
        return if (value.isNullOrEmpty()) null else Language.valueOf(value)
    }

    @Synchronized
    @TypeConverter
    fun toRankValue(rank: Rank?): String? {
        return if (rank == null) null else rank.name
    }

    @Synchronized
    @TypeConverter
    fun toRank(value: String?): Rank? {
        return if (value.isNullOrEmpty()) null else Rank.valueOf(value)
    }

    @Synchronized
    @TypeConverter
    fun toLevelValue(level: Level?): String? {
        return if (level == null) null else level.name
    }

    @Synchronized
    @TypeConverter
    fun toLevel(value: String?): Level? {
        return if (value.isNullOrEmpty()) null else Level.valueOf(value)
    }

    @Synchronized
    @TypeConverter
    fun toQualityValue(quality: Quality?): String? {
        return if (quality == null) null else quality.name
    }

    @Synchronized
    @TypeConverter
    fun toQuality(value: String?): Quality? {
        return if (value.isNullOrEmpty()) null else Quality.valueOf(value)
    }
}