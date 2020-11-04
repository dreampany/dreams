package com.dreampany.radio.data.source.remote

import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.radio.api.radiobrowser.StationService
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.data.source.api.StationDataSource
import com.dreampany.radio.data.source.mapper.StationMapper
import java.net.UnknownHostException

/**
 * Created by roman on 1/11/20
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
    override suspend fun write(input: Station): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<Station>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsByCountry(
        country: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun readsByCountryCode(
        countryCode: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        try {
            val response = service.readsByCountryCode(
                countryCode,
                order,
                offset,
                limit
            ).execute()
            if (response.isSuccessful) {
                val data = response.body() ?: return null
                return mapper.reads(data)
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

    override suspend fun readsByLanguage(
        language: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun readsTrend(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        try {
            val response = service.readsByTopClick(
                limit
            ).execute()
            if (response.isSuccessful) {
                val data = response.body() ?: return null
                return mapper.reads(data)
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
    override suspend fun readsPopular(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        try {
            val response = service.readsByTopVote(
                limit
            ).execute()
            if (response.isSuccessful) {
                val data = response.body() ?: return null
                return mapper.reads(data)
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
    override suspend fun readsRecent(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        try {
            val response = service.readsByLastClick(
                limit
            ).execute()
            if (response.isSuccessful) {
                val data = response.body() ?: return null
                return mapper.reads(data)
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
    override suspend fun readsChange(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        try {
            val response = service.readsByLastChange(
                limit
            ).execute()
            if (response.isSuccessful) {
                val data = response.body() ?: return null
                return mapper.reads(data)
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

    override suspend fun searchByName(
        name: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun searchByTag(
        tag: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

}