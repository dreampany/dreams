package com.dreampany.tools.data.source.news.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.model.news.Page
import com.dreampany.tools.misc.constants.Constants
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
class NewsPref
@Inject constructor(
    context: Context,
    private val gson: Gson
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.News.PREF

    @Synchronized
    fun commitPagesSelection() {
        setPrivately(Constants.Keys.Pref.News.PAGE, true)
    }

    val isPagesSelected: Boolean
        get() = getPrivately(Constants.Keys.Pref.News.PAGE, Constant.Default.BOOLEAN)

    val expireTimeOfCategory: Long
        get() {
            val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
                append(Constants.Keys.Pref.News.CATEGORY)
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

    @Synchronized
    fun commitPages(inputs: List<Page>) {
        val json = gson.toJson(inputs)
        setPrivately(Constants.Keys.Pref.News.PAGES, json)
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
                getPrivately(Constants.Keys.Pref.News.PAGES, Constant.Default.NULL as String?)
            if (json.isNullOrEmpty()) {
                return null
            } else {
                return gson.fromJson(json, Array<Page>::class.java).toList()
            }
        }
}