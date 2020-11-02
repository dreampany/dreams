package com.dreampany.radio.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.radio.data.model.Page
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.misc.Constants
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Prefs
@Inject constructor(
    context: Context,
    private val gson: Gson
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.PREF

    @Synchronized
    fun writePagesSelection() {
        setPrivately(Constants.Keys.Pref.PAGE, true)
    }

    val isPagesSelected: Boolean
        get() = getPrivately(Constants.Keys.Pref.PAGE, Constant.Default.BOOLEAN)

    @Synchronized
    fun writePages(inputs: List<Page>) {
        val json = gson.toJson(inputs)
        setPrivately(Constants.Keys.Pref.PAGES, json)
    }

    @Synchronized
    fun writePage(input: Page) {
        val pages = pages
        val inputs = if (pages.isNullOrEmpty()) arrayListOf<Page>() else ArrayList(pages)
        inputs.add(input)
        writePages(inputs)
    }

    val pages: List<Page>?
        get() {
            val json =
                getPrivately(Constants.Keys.Pref.PAGES, Constant.Default.NULL as String?)
            if (json.isNullOrEmpty()) {
                return null
            } else {
                return gson.fromJson(json, Array<Page>::class.java).toList()
            }
        }

    @Synchronized
    fun writeExpireTime(type: Page.Type) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun writeExpireTime(type: Page.Type, order: Station.Order, offset: Long) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
            append(order.value)
            append(offset)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun writeExpireTime(
        type: Page.Type,
        order: Station.Order,
        countryCode: String,
        offset: Long
    ) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
            append(order.value)
            append(countryCode)
            append(offset)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun readExpireTime(type: Page.Type): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun readExpireTime(
        type: Page.Type, order: Station.Order, offset: Long
    ): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
            append(order.value)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun readExpireTime(
        type: Page.Type,
        order: Station.Order,
        countryCode: String,
        offset: Long
    ): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
            append(order.value)
            append(countryCode)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }
}