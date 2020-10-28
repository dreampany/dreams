package com.dreampany.news.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.framework.misc.util.Util
import com.dreampany.news.data.model.Page
import com.dreampany.news.misc.Constants
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
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
    fun commitPagesSelection() {
        setPrivately(Constants.Keys.Pref.PAGE, true)
    }

    val isPagesSelected: Boolean
        get() = getPrivately(Constants.Keys.Pref.PAGE, Constant.Default.BOOLEAN)

    @Synchronized
    fun commitPages(inputs: List<Page>) {
        val json = gson.toJson(inputs)
        setPrivately(Constants.Keys.Pref.PAGES, json)
    }

    @Synchronized
    fun commitPage(input: Page) {
        val pages = pages
        val inputs = if (pages.isNullOrEmpty()) arrayListOf<Page>() else ArrayList(pages)
        inputs.add(input)
        commitPages(inputs)
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

    fun commitExpireTimeOfCategory() {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(Constants.Keys.Pref.CATEGORY)
        }
        setPrivately(key.toString(), currentMillis)
    }

    val expireTimeOfCategory: Long
        get() {
            val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
                append(Constants.Keys.Pref.CATEGORY)
            }
            return getPrivately(key.toString(), Constant.Default.LONG)
        }

    @Synchronized
    fun getExpireTime(query: String, language: String, offset: Long): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(query)
            append(language)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(query: String, language: String, offset: Long) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(query)
            append(language)
            append(offset)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }
}