package com.dreampany.frame.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


/**
 * Created by roman on 2019-06-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Json {
    companion object {
        inline fun <reified T : Any?> parseList(json: String): List<T>? {
            val gson = Gson()
            val typeToken = object : TypeToken<List<T>>() {}.type
            val data: List<T> = gson.fromJson(json, typeToken)
            return data
        }
    }
}