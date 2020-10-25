package com.dreampany.tube.data.source.api

import com.dreampany.tube.data.model.Search

/**
 * Created by roman on 25/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface SearchDataSource {

    @Throws
    suspend fun write(input: Search): Long

    @Throws
    suspend fun hit(id : String, ref: String): Long
}