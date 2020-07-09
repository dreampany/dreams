package com.dreampany.tube.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.tube.misc.AppConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AppPref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String = AppConstants.Keys.Pref.PREF

    @Synchronized
    fun commitCategory() {
        setPrivately(AppConstants.Keys.Pref.CATEGORY, true)
    }

    @Synchronized
    fun isCategory(): Boolean {
        return getPrivately(AppConstants.Keys.Pref.CATEGORY, Constants.Default.BOOLEAN)
    }


    @Synchronized
    fun commitExpireTimeOfCategory() {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(AppConstants.Keys.Pref.CATEGORY)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun getExpireTimeOfCategory(): Long {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(AppConstants.Keys.Pref.CATEGORY)
        }
        return getPrivately(key.toString(), Constants.Default.LONG)
    }

    @Synchronized
    fun commitExpireTimeOfSearch() {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(AppConstants.Keys.Pref.CATEGORY)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun getExpireTimeOfSearch(query: String): Long {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(AppConstants.Keys.Pref.SEARCH)
            append(query)
        }
        return getPrivately(key.toString(), Constants.Default.LONG)
    }

    @Synchronized
    fun commitExpireTimeOfCategoryId(categoryId: String, offset: Long) {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(AppConstants.Keys.Pref.VIDEO)
            append(categoryId)
            append(offset)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun getExpireTimeOfCategoryId(categoryId: String, offset: Long): Long {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(AppConstants.Keys.Pref.VIDEO)
            append(categoryId)
            append(offset)
        }
        return getPrivately(key.toString(), Constants.Default.LONG)
    }

    @Synchronized
    fun commitExpireTimeOfVideo(id: String) {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(AppConstants.Keys.Pref.VIDEO)
            append(id)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun getExpireTimeOfVideo(id: String): Long {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(AppConstants.Keys.Pref.VIDEO)
            append(id)
        }
        return getPrivately(key.toString(), Constants.Default.LONG)
    }

}