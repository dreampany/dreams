package com.dreampany.tools.data.source.wifi.room

import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.data.source.wifi.api.WifiDataSource
import com.dreampany.tools.data.source.wifi.mapper.WifiMapper
import com.dreampany.tools.data.source.wifi.room.dao.WifiDao

/**
 * Created by roman on 24/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiRoomDataSource
constructor(
    private val mapper: WifiMapper,
    private val dao: WifiDao
) : WifiDataSource {

    @Throws
    override suspend fun put(input: Wifi): Long {
        mapper.add(input)
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun put(inputs: List<Wifi>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(put(it)) }
        return result
    }

    override suspend fun gets(): List<Wifi>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(callback: () -> Unit): List<Wifi>? = dao.items


    @Throws
    override suspend fun gets(offset: Long, limit: Long, callback: () -> Unit): List<Wifi>? =
        mapper.gets(this, offset, limit, callback)
}