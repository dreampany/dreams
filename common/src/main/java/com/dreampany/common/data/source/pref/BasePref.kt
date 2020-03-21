package com.dreampany.common.data.source.pref

import android.content.Context
import com.dreampany.common.misc.constant.Constants
import com.github.pwittchen.prefser.library.rx2.Prefser

/**
 * Created by roman on 6/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BasePref(val context: Context) {

    private val publicPref: Prefser
    private val privatePref: Prefser

    init {
        publicPref = Prefser(context)
        val prefName = getPrivateName(context)
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        privatePref = Prefser(pref)
    }

    open fun getPrivateName(context: Context): String {
        return Constants.Keys.PrefKeys.DEFAULT
    }

    fun hasPublic(key: String): Boolean {
        return publicPref.contains(key)
    }

    fun hasPrivate(key: String): Boolean {
        return privatePref.contains(key)
    }

    /* boolean */
    fun setPublicly(key: String, value: Boolean) {
        publicPref.put(key, value)
    }

    fun getPublicly(key: String, defaultValue: Boolean): Boolean {
        return publicPref.preferences.getBoolean(key, defaultValue)
    }

    fun setPrivately(key: String, value: Boolean) {
        privatePref.put(key, value)
    }

    fun getPrivately(key: String, defaultValue: Boolean): Boolean {
        return privatePref.preferences.getBoolean(key, defaultValue)
    }

    /* int */
    fun setPublicly(key: String, value: Int) {
        publicPref.put(key, value)
    }

    fun getPublicly(key: String, defaultValue: Int): Int {
        return publicPref.preferences.getInt(key, defaultValue)
    }

    /* long */
    fun setPrivately(key: String, value: Long) {
        privatePref.put(key, value)
    }

    fun getPrivately(key: String, defaultValue: Long): Long {
        return privatePref.preferences.getLong(key, defaultValue)
    }

    /* string */
    fun setPublicly(key: String, value: String) {
        publicPref.put(key, value)
    }

    fun getPublicly(key: String, defaultValue: String): String {
        return publicPref.get(key, String::class.java, defaultValue)
    }

    /* custom */
    fun <T> setPublicly(key: String, value: T) {
        publicPref.put(key, value)
    }

    fun <T> getPublicly(key: String, classOfT: Class<T>, defaultValue: T): T {
        return publicPref.get(key, classOfT, defaultValue)
    }

    fun <T> setPrivately(key: String, value: T) {
        privatePref.put(key, value)
    }

    fun <T> getPrivately(key: String, classOfT: Class<T>, defaultValue: T): T {
        return privatePref.get(key, classOfT, defaultValue)
    }
}