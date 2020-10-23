package com.dreampany.tools.data.source.news.api

import com.dreampany.tools.data.model.news.Category

/**
 * Created by roman on 14/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CategoryDataSource {
    @Throws
    suspend fun gets(): List<Category>?
}