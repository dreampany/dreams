package com.dreampany.tools.data.source.news.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.model.misc.Category
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.misc.constants.NewsConstants
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
) : BasePref(context) {

    override fun getPrivateName(context: Context): String = NewsConstants.Keys.Pref.NEWS

    fun commitCategoriesSelection() {
        setPrivately(Constants.Keys.Pref.News.CATEGORY, true)
    }

    val isCategoriesSelected: Boolean
        get() = getPrivately(Constants.Keys.Pref.News.CATEGORY, Constant.Default.BOOLEAN)

    fun commitExpireTimeOfCategory() {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(Constants.Keys.Pref.News.CATEGORY)
        }
        setPrivately(key.toString(), currentMillis)
    }

    val expireTimeOfCategory: Long
        get() {
            val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
                append(Constants.Keys.Pref.News.CATEGORY)
            }
            return getPrivately(key.toString(), Constant.Default.LONG)
        }

    @Synchronized
    fun getExpireTime(query: String, language: String, offset: Long): Long {
        val key = StringBuilder(NewsConstants.Keys.Pref.EXPIRE).apply {
            append(query)
            append(language)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(query: String, language: String, offset: Long) {
        val key = StringBuilder(NewsConstants.Keys.Pref.EXPIRE).apply {
            append(query)
            append(language)
            append(offset)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }

    @Synchronized
    fun commitCategories(inputs: List<Category>) {
        val json = gson.toJson(inputs)
        setPrivately(Constants.Keys.Pref.News.CATEGORIES, json)
    }

    val categories: List<Category>?
        get() {
            val json =
                getPrivately(Constants.Keys.Pref.News.CATEGORIES, Constant.Default.NULL as String?)
            if (json.isNullOrEmpty()) {
                return null
            } else {
                return gson.fromJson(json, Array<Category>::class.java).toList()
            }
        }
}