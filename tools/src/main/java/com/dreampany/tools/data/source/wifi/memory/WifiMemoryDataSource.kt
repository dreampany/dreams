package com.dreampany.tools.data.source.wifi.memory

import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.data.source.wifi.api.WifiDataSource
import com.dreampany.tools.data.source.wifi.mapper.WifiMapper

/**
 * Created by roman on 24/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiMemoryDataSource(
    private val mapper: WifiMapper,
    private val provider: WifiProvider
) : WifiDataSource {

    @Throws
    override suspend fun put(input: Wifi): Long {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun put(inputs: List<Wifi>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(): List<Wifi>? {
        provider.startScan
        return mapper.gets(provider.scanResults)
    }

    @Throws
    override suspend fun gets(callback: () -> Unit): List<Wifi>? {
        provider.enable(callback)
        provider.startScan
        return mapper.gets(provider.scanResults)
    }


    @Throws
    override suspend fun gets(offset: Long, limit: Long, callback: () -> Unit): List<Wifi>? {
        TODO("Not yet implemented")
    }
}