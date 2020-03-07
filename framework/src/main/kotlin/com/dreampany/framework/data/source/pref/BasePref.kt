package com.dreampany.framework.data.source.pref

import android.content.Context
import com.dreampany.framework.misc.Constants
import com.github.pwittchen.prefser.library.rx2.Prefser
import com.github.pwittchen.prefser.library.rx2.TypeToken
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
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
        return Constants.Pref.DEFAULT
    }

    fun hasPublic(key: String): Boolean {
        return publicPref.contains(key)
    }

    fun hasPrivate(key: String): Boolean {
        return privatePref.contains(key)
    }

    /* setter */
    fun setPublicly(key: String, value: Int) {
        publicPref.put(key, value)
    }

    fun setPublicly(key: String, value: String) {
        publicPref.put(key, value)
    }

    fun <T> setPublicly(key: String, value: T) {
        publicPref.put(key, value)
    }

    fun setPrivately(key: String, value: Int) {
        privatePref.put(key, value)
    }

    fun setPrivately(key: String, value: Long) {
        privatePref.put(key, value)
    }

    fun setPrivately(key: String, value: String) {
        privatePref.put(key, value)
    }

    fun <T> setPrivately(key: String, value: T) {
        privatePref.put(key, value)
    }

    fun getPublicly(key: String, defaultValue: Int): Int {
        return publicPref.preferences.getInt(key, defaultValue)
    }

    fun getPublicly(key: String, defaultValue: Long): Long {
        return publicPref.preferences.getLong(key, defaultValue)
    }

    fun getPublicly(key: String, defaultValue: Boolean): Boolean {
        return publicPref.preferences.getBoolean(key, defaultValue)
    }

    fun getPrivately(key: String, defaultValue: Boolean): Boolean {
        return privatePref.preferences.getBoolean(key, defaultValue)
    }

    fun getPrivately(key: String, defaultValue: Int): Int {
        return privatePref.preferences.getInt(key, defaultValue)
    }


    fun getPrivately(key: String, defaultValue: String): String {
        return privatePref.get(key, String::class.java, defaultValue)
    }


    fun getPrivately(key: String, defaultValue: Long): Long {
        return privatePref.preferences.getLong(key, defaultValue)
    }

    fun getPublicly(key: String, defaultValue: String): String {
        return publicPref.get(key, String::class.java, defaultValue)
    }

    fun <T> getPublicly(key: String, classOfT: Class<T>, defaultValue: T): T {
        return publicPref.get(key, classOfT, defaultValue)
    }

    fun <T> getPrivately(key: String, classOfT: Class<T>, defaultValue: T): T {
        return privatePref.get(key, classOfT, defaultValue)
    }

    fun <T> getPrivately(key: String, typeTokenOfT: TypeToken<T>, defaultValue: T): T {
        return privatePref.get(key, typeTokenOfT, defaultValue)
    }

    fun <T> putPrivately(key: String, value: T) {
        privatePref.put(key, value)
    }

    fun removePrivately(key: String) {
        privatePref.remove(key)
    }

    fun <T> observePublicly(key: String, classOfT: Class<T>, defaultValue: T): Flowable<T> {
        return publicPref.observe(key, classOfT, defaultValue)
            .toFlowable(BackpressureStrategy.LATEST)
    }

    fun <T> observePrivately(key: String, classOfT: Class<T>, defaultValue: T): Flowable<T> {
        return privatePref.observe(key, classOfT, defaultValue)
            .toFlowable(BackpressureStrategy.LATEST)
    }
}