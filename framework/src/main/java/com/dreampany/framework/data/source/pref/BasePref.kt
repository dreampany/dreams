package com.dreampany.framework.data.source.pref

import android.content.Context
import com.dreampany.framework.misc.constant.Constant
import com.github.pwittchen.prefser.library.rx2.Prefser

/**
 * Created by roman on 6/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BasePref(val context: Context) {

    private val public: Prefser
    private val private: Prefser

    init {
        public = Prefser(context)
        val prefName = getPrivateName(context)
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        private = Prefser(pref)
    }

    open fun getPrivateName(context: Context): String = Constant.Keys.Pref.DEFAULT

    /* check */
    fun hasPublic(key: String): Boolean = public.contains(key)

    fun hasPrivate(key: String): Boolean = private.contains(key)

    /* check */
    fun removePublic(key: String) = public.remove(key)
    fun removePrivate(key: String) = private.remove(key)

    /* boolean */
    fun setPublicly(key: String, value: Boolean) {
        public.put(key, value)
    }

    fun getPublicly(key: String, defaultValue: Boolean): Boolean {
        return public.preferences.getBoolean(key, defaultValue)
    }

    fun setPrivately(key: String, value: Boolean) {
        private.put(key, value)
    }

    fun getPrivately(key: String, defaultValue: Boolean): Boolean {
        return private.preferences.getBoolean(key, defaultValue)
    }

    /* int */
    fun setPublicly(key: String, value: Int) {
        public.put(key, value)
    }

    fun getPublicly(key: String, defaultValue: Int): Int {
        return public.preferences.getInt(key, defaultValue)
    }

    /* long */
    fun setPrivately(key: String, value: Long) {
        private.put(key, value)
    }

    fun getPrivately(key: String, defaultValue: Long): Long {
        return private.preferences.getLong(key, defaultValue)
    }

    /* string */
    fun setPublicly(key: String, value: String) {
        public.put(key, value)
    }

    fun getPublicly(key: String, defaultValue: String): String {
        return public.get(key, String::class.java, defaultValue)
    }

    fun setPrivately(key: String, value: String) {
        private.put(key, value)
    }

    fun getPrivately(key: String, defaultValue: String?): String? {
        return private.get(key, String::class.java, defaultValue)
    }

    /* custom */
    fun <T> setPublicly(key: String, value: T) {
        public.put(key, value)
    }

    fun <T> getPublicly(key: String, classOfT: Class<T>, defaultValue: T?): T? {
        return public.get(key, classOfT, defaultValue)
    }

    fun <T> setPrivately(key: String, value: T) {
        private.put(key, value)
    }

    fun <T> getPrivately(key: String, classOfT: Class<T>, defaultValue: T?): T? {
        return private.get(key, classOfT, defaultValue)
    }
}