package com.dreampany.frame.data.misc

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Mapper {
    @Inject
    protected lateinit var gson: Gson

    private val mapType: Type

    init {
        mapType = object : TypeToken<Map<String, Any>>() {}.type
    }

    fun toJson(map: Map<String, Any>?): String? {
        if (map.isNullOrEmpty()) return null
        return gson.toJson(map, mapType)
    }

    fun toMap(json: String?): Map<String, Any>? {
        if (json.isNullOrEmpty()) return null
        return gson.fromJson(json, mapType)
    }
}