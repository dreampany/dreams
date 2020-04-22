package com.dreampany.tools.data.source.radio.remote

import com.dreampany.common.misc.func.Parser
import com.dreampany.common.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.radio.StationService
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.data.source.radio.api.StationDataSource
import com.dreampany.tools.data.source.radio.mapper.StationMapper
import java.net.UnknownHostException

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationRemoteDataSource(
    private val network: NetworkManager,
    private val parser: Parser,
    private val mapper: StationMapper,
    private val service: StationService
) : StationDataSource {
    override suspend fun putItem(item: Station): Long {
        TODO("Not yet implemented")
    }

    override suspend fun putItems(items: List<Station>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun getItems(): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun getItems(countryCode: String): List<Station>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getItemsOfCountry(
        countryCode: String,
        hideBroken: Boolean,
        order: StationOrder,
        reverse: Boolean,
        offset: Long,
        limit: Long
    ): List<Station>? {
        try {
            val response = service.getItemsOfCountry(
                countryCode,
                hideBroken,
                order.value,
                reverse,
                offset,
                limit
            ).execute()
            if (response.isSuccessful) {
                val data = response.body() ?: return null
                return mapper.getItems(data)
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

    @Throws
    override suspend fun getItemsOfTrends(limit: Long): List<Station>? {
        try {
            val response = service.getItemsOfTrends(limit).execute()
            if (response.isSuccessful) {
                val data = response.body() ?: return null
                return mapper.getItems(data)
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

    @Throws
    override suspend fun getItemsOfPopular(limit: Long): List<Station>? {
        try {
            val response = service.getItemsOfPopular(limit).execute()
            if (response.isSuccessful) {
                val data = response.body() ?: return null
                return mapper.getItems(data)
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