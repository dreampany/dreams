package com.dreampany.common.data.source.api

import com.dreampany.common.data.model.Time

/**
 * Created by roman on 7/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
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