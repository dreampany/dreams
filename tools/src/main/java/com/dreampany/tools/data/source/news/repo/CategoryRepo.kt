package com.dreampany.tools.data.source.news.repo

import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tools.api.news.misc.Constants
import com.dreampany.tools.data.model.misc.Category
import com.dreampany.tools.data.source.news.api.CategoryDataSource
import com.dreampany.tools.data.source.news.mapper.CategoryMapper
import com.dreampany.tools.data.source.news.pref.NewsPref
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
    private val pref: NewsPref,
    private val mapper: CategoryMapper
) : CategoryDataSource {

    @Throws
    override suspend fun gets() = withContext(Dispatchers.IO) {
        getsImpl()
    }

    private fun getsImpl(): List<Category> {
        return Constants.Values.CATEGORIES.map {
            val category = Category(it)
            category.category = it
            category
        }
    }
}