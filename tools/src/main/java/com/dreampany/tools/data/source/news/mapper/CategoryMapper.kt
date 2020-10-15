package com.dreampany.tools.data.source.news.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.tools.data.model.misc.Category
import com.dreampany.tools.data.source.news.pref.NewsPref
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashSet

/**
 * Created by roman on 14/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CategoryMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: NewsPref
) {
    private val categories: MutableMap<String, Category>
    private val favorites: MutableMap<String, Boolean>
    private val countries : HashSet<String>

    init {
        categories = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
        countries = HashSet(Locale.getISOCountries().asList())
    }

    fun isIsoCountry(country : String) : Boolean = countries.contains(country)

    val isExpired: Boolean
        get() {
            val time = pref.expireTimeOfCategory
            return time.isExpired(Constants.Times.News.CATEGORIES)
        }
}