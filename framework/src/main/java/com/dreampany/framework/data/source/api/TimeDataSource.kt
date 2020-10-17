package com.dreampany.framework.data.source.api

import com.dreampany.framework.data.model.Time

/**
 * Created by roman on 28/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface TimeDataSource {

    @Throws
    suspend fun write(item: Time): Long

    @Throws
    suspend fun read(id: String, type: String, subtype: String, state: String): Time?

    @Throws
    suspend fun readTime(id: String, type: String, subtype: String, state: String): Long
}