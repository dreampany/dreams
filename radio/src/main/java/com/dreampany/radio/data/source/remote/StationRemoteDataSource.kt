package com.dreampany.radio.data.source.remote

import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.radio.api.radiobrowser.StationService
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.data.source.api.StationDataSource
import com.dreampany.radio.data.source.mapper.StationMapper

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
        countryCode: String,
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsByTop(
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsByTrend(
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsByPopular(
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun searchByName(
        name: String,
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun searchByTag(
        tag: String,
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun searchByLanguage(
        language: String,
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

}