package com.dreampany.frame.data.source.room

import androidx.room.TypeConverter
import com.dreampany.frame.data.enums.*
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

    @TypeConverter
    @Synchronized
    fun toTypeValue(type: Type?): String? {
        return if (type == null) null else type.name
    }

    @TypeConverter
    @Synchronized
    fun toType(value: String?): Type? {
        return if (value.isNullOrEmpty()) null else Type.valueOf(value)
    }

    @TypeConverter
    @Synchronized
    fun toSubtypeValue(subtype: Subtype?): String? {
        return if (subtype == null) null else subtype.name
    }

    @TypeConverter
    @Synchronized
    fun toSubtype(value: String?): Subtype? {
        return if (value.isNullOrEmpty()) null else Subtype.valueOf(value)
    }

    @TypeConverter
    @Synchronized
    fun toStateValue(state: State?): String? {
        return if (state == null) null else state.name
    }

    @TypeConverter
    @Synchronized
    fun toState(value: String?): State? {
        return if (value.isNullOrEmpty()) null else State.valueOf(value)
    }

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
        return if (json.isNullOrEmpty()) {
            null
        } else gson.fromJson<ArrayList<String>>(json, type)
    }

    @TypeConverter
    @Synchronized
    fun toLanguageValue(language: Language?): String? {
        return if (language == null) null else language.name
    }

    @TypeConverter
    @Synchronized
    fun toLanguage(value: String?): Language? {
        return if (value.isNullOrEmpty()) null else Language.valueOf(value)
    }

    @TypeConverter
    @Synchronized
    fun toRankValue(rank: Rank?): String? {
        return if (rank == null) null else rank.name
    }

    @TypeConverter
    @Synchronized
    fun toRank(value: String?): Rank? {
        return if (value.isNullOrEmpty()) null else Rank.valueOf(value)
    }

    @TypeConverter
    @Synchronized
    fun toLevelValue(level: Level?): String? {
        return if (level == null) null else level.name
    }

    @TypeConverter
    @Synchronized
    fun toLevel(value: String?): Level? {
        return if (value.isNullOrEmpty()) null else Level.valueOf(value)
    }
}