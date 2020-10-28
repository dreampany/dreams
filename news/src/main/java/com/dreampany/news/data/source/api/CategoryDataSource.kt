package com.dreampany.news.data.source.api

import com.dreampany.news.data.model.Category

/**
 * Created by roman on 14/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CategoryDataSource {
    @Throws
    suspend fun reads(): List<Category>?
}