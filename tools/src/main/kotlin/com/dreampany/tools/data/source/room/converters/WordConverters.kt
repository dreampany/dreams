package com.dreampany.tools.data.source.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converters.Converters
import com.dreampany.tools.data.model.Definition
import com.google.gson.reflect.TypeToken

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WordConverters : Converters() {

    private val defType = object : TypeToken<ArrayList<Definition>>() {}.type

    @TypeConverter
    @Synchronized
    fun toDefString(definitions: ArrayList<Definition>?): String? {
        return if (definitions.isNullOrEmpty()) null
        else gson.toJson(definitions, defType)
    }

    @TypeConverter
    @Synchronized
    fun toDefList(json: String?): ArrayList<Definition>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson(json, defType)
    }
}