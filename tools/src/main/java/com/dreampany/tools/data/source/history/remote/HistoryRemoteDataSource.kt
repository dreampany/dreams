package com.dreampany.tools.data.source.history.remote

import android.content.Context
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.history.MuffinService
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistorySubtype
import com.dreampany.tools.data.enums.history.HistoryType
import com.dreampany.tools.data.model.history.History
import com.dreampany.tools.data.source.history.api.HistoryDataSource
import com.dreampany.tools.data.source.history.mapper.HistoryMapper
import java.net.UnknownHostException

/**
 * Created by roman on 10/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryRemoteDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: HistoryMapper,
    private val service: MuffinService
) : HistoryDataSource {
    override suspend fun isFavorite(input: History): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: History): Boolean {
        TODO("Not yet implemented")
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

    @Throws
    override suspend fun gets(
        source: HistorySource,
        type: HistoryType,
        subtype: HistorySubtype,
        month: Int,
        day: Int
    ): List<History>? {
        try {
            val response = service.getWikiHistory(month, day).execute()
            if (response.isSuccessful) {
                val body = response.body() ?: return null
                val data = body.data ?: return null
                return mapper.gets(data, source, type, subtype, body.date, body.url)
            } else {
                throw SmartError()
            }
        } catch (error: Throwable) {
            if (error is SmartError) throw error
            if (error is UnknownHostException) throw SmartError(
                message = error.message,
                error = error
            )
        }
        throw SmartError()
    }

}