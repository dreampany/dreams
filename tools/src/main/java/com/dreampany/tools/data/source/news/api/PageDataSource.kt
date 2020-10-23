package com.dreampany.tools.data.source.news.api

import com.dreampany.tools.data.model.news.Page

/**
 * Created by roman on 22/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface PageDataSource {

    @Throws
    suspend fun write(input: Page): Long

    @Throws
    suspend fun write(inputs: List<Page>): List<Long>?

    @Throws
    suspend fun read(id: String): Page?

    @Throws
    suspend fun reads(): List<Page>?
}