package com.dreampany.news.data.source.repo

import com.dreampany.framework.misc.exts.title
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.news.api.news.misc.Constants
import com.dreampany.news.data.model.Category
import com.dreampany.news.data.source.api.CategoryDataSource
import com.dreampany.news.data.source.mapper.CategoryMapper
import com.dreampany.news.data.source.pref.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 15/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CategoryRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: Prefs,
    private val mapper: CategoryMapper
) : CategoryDataSource {

    @Throws
    override suspend fun reads() = withContext(Dispatchers.IO) {
        readsImpl()
    }

    private fun readsImpl(): List<Category>? {
        return Constants.Values.CATEGORIES.map {
            val category = Category(it)
            category.title = it.title
            category
        }
    }
}