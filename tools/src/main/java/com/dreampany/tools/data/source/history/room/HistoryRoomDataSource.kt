package com.dreampany.tools.data.source.history.room

import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistoryState
import com.dreampany.tools.data.model.history.History
import com.dreampany.tools.data.source.history.api.HistoryDataSource
import com.dreampany.tools.data.source.history.mapper.HistoryMapper
import com.dreampany.tools.data.source.history.room.dao.HistoryDao

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryRoomDataSource(
    private val mapper: HistoryMapper,
    private val dao: HistoryDao
) : HistoryDataSource {

    @Throws
    override suspend fun isFavorite(input: History): Boolean = mapper.isFavorite(input)

    @Throws
    override suspend fun toggleFavorite(input: History): Boolean {
        val favorite = isFavorite(input)
        if (favorite) {
            mapper.deleteFavorite(input)
        } else {
            mapper.putFavorite(input)
        }
        return favorite.not()
    }

    override suspend fun getFavorites(): List<History>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun put(input: History): Long {
        mapper.add(input)
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun put(inputs: List<History>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(put(it)) }
        return result    }

    override suspend fun get(id: String): History? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(): List<History>? = dao.items

    override suspend fun gets(ids: List<String>): List<History>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(
        source: HistorySource,
        state: HistoryState,
        month: Int,
        day: Int
    ): List<History>? = dao.getItems(source, state, month, day)

    /*@Throws


    @Throws
    override suspend fun put(input: History): Long {
        mapper.add(input)
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun put(inputs: List<History>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(put(it)) }
        return result
    }*/
}
