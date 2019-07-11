package com.dreampany.frame.data.source.pref

import android.content.Context
import com.dreampany.frame.util.AndroidUtil
import com.github.pwittchen.prefser.library.rx2.Prefser

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BasePrefKt {

    private val publicPref: Prefser
    private val privatePref: Prefser

    constructor(context: Context) {
        publicPref = Prefser(context)
        val prefName = getPrivatePrefName(context)
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        privatePref = Prefser(pref)
    }

    protected fun getPrivatePrefName(context: Context): String? {
        return AndroidUtil.getPackageName(context)
    }

    fun hasPublic(key:String) : Boolean {
        return publicPref.contains(key)
    }

    fun hasPrivate(key:String) : Boolean {
        return privatePref.contains(key)
    }

    fun <T> setPublicly(key: String, value: T) {
        publicPref.put(key, value)
    }

    fun <T> setPrivately(key: String, value: T) {
        privatePref.put(key, value)
    }

    fun <T> getPublicly(key: String, classOfT: Class<T>, defaultValue: T): T {
        return publicPref.get(key, classOfT, defaultValue)
    }

    fun <T> getPrivately(key: String, classOfT: Class<T>, defaultValue: T): T {
        return privatePref.get(key, classOfT, defaultValue)
    }
}