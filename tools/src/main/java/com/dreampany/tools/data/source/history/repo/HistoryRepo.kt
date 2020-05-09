package com.dreampany.tools.data.source.history.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistorySubtype
import com.dreampany.tools.data.enums.history.HistoryType
import com.dreampany.tools.data.model.history.History
import com.dreampany.tools.data.source.history.api.HistoryDataSource
import com.dreampany.tools.data.source.history.mapper.HistoryMapper
import com.dreampany.tools.data.source.history.pref.HistoryPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 10/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class HistoryRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: HistoryPref,
    private val mapper: HistoryMapper,
    @Room private val room: HistoryDataSource,
    @Remote private val remote: HistoryDataSource
) : HistoryDataSource {
    override suspend fun isFavorite(input: History)= withContext(Dispatchers.IO) {
        room.isFavorite(input)
    }

    override suspend fun toggleFavorite(input: History)= withContext(Dispatchers.IO) {
        room.toggleFavorite(input)
    }

    override suspend fun getFavorites(): List<History>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: History): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<History>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): History? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<History>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(ids: List<String>): List<History>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(
        source: HistorySource,
        type: HistoryType,
        subtype: HistorySubtype,
        month: Int,
        day: Int
    )= withContext(Dispatchers.IO) {
        if (mapper.isExpired(source, type, subtype, month, day)) {
            val result = remote.gets(source, type, subtype, month, day)
            if (!result.isNullOrEmpty()) {
                mapper.commitExpire(source, type, subtype, month, day)
                room.put(result)
            }
        }
        room.gets(source, type, subtype, month, day)
    }
}