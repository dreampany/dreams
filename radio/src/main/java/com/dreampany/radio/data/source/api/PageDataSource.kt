package com.dreampany.radio.data.source.api

import com.dreampany.radio.data.model.Page

/**
 * Created by roman on 1/11/20
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