package com.dreampany.radio.data.source.room

import com.dreampany.radio.data.model.Station
import com.dreampany.radio.data.source.api.StationDataSource
import com.dreampany.radio.data.source.room.dao.StationDao

/**
 * Created by roman on 2/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationRoomDataSource(
     private val dao: StationDao
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

    override suspend fun readsByCountryCode(
        countryCode: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsByLanguage(
        language: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsTrend(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsPopular(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsRecent(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsChange(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
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