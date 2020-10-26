package com.dreampany.tools.data.source.misc.api

import com.dreampany.tools.data.model.misc.Search


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