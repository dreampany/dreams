package com.dreampany.tools.data.source.wifi.api

import com.dreampany.tools.data.model.wifi.Wifi

/**
 * Created by roman on 23/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface WifiDataSource {

    @Throws
    suspend fun put(input: Wifi): Long

    @Throws
    suspend fun put(inputs: List<Wifi>): List<Long>?

    @Throws
    suspend fun gets(): List<Wifi>?

    @Throws
    suspend fun gets(callback: () -> Unit): List<Wifi>?

    @Throws
    suspend fun gets(offset: Long, limit: Long, callback: () -> Unit): List<Wifi>?
}