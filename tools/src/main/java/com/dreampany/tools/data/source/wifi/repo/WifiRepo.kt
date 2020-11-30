package com.dreampany.tools.data.source.wifi.repo

import com.dreampany.framework.inject.annote.Memory
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.data.source.wifi.api.WifiDataSource
import com.dreampany.tools.data.source.wifi.mapper.WifiMapper
import com.dreampany.tools.data.source.wifi.pref.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 25/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WifiRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: Prefs,
    private val mapper: WifiMapper,
    @Memory private val memory: WifiDataSource,
    @Room private val room: WifiDataSource
) : WifiDataSource {
    override suspend fun put(input: Wifi): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Wifi>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets() = withContext(Dispatchers.IO) {
        val result = memory.gets()
        if (!result.isNullOrEmpty()) {
            room.put(result)
        }
        result
    }

    override suspend fun gets(callback: () -> Unit): List<Wifi>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(
        offset: Long,
        limit: Long,
        callback: () -> Unit
    ) = withContext(Dispatchers.IO) {
        val result = memory.gets(callback)
        if (!result.isNullOrEmpty()) {
            room.put(result)
        }
        room.gets(offset, limit, callback)
    }

}