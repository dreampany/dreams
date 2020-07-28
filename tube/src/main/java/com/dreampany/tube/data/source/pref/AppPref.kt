package com.dreampany.tube.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.misc.AppConstants
import com.google.gson.Gson
import java.util.*
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
    context: Context,
    private val gson: Gson
) : BasePref(context) {

    override fun getPrivateName(context: Context): String = AppConstants.Keys.Pref.PREF

    /*@Synchronized
    fun commitRegionCode(regionCode : String) {
        setPrivately(AppConstants.Keys.Pref.REGION_CODE, regionCode)
    }

    val regionCode: String?
        get() = getPrivately(AppConstants.Keys.Pref.REGION_CODE, Locale.US.country)*/

    @Synchronized
    fun commitCategoriesSelection() {
        setPrivately(AppConstants.Keys.Pref.CATEGORY, true)
    }

    val isCategoriesSelected: Boolean
        get() = getPrivately(AppConstants.Keys.Pref.CATEGORY, Constants.Default.BOOLEAN)

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

    @Synchronized
    fun commitCategories(inputs: List<Category>) {
        val json = gson.toJson(inputs)
        setPrivately(AppConstants.Keys.Pref.CATEGORIES, json)
    }

    val categories: List<Category>?
        get() {
            val json =
                getPrivately(AppConstants.Keys.Pref.CATEGORIES, Constants.Default.NULL as String?)
            if (json.isNullOrEmpty()) {
                return null
            } else {
                return gson.fromJson(json, Array<Category>::class.java).toList()
            }
        }

}