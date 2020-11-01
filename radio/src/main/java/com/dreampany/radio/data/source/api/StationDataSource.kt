package com.dreampany.radio.data.source.api

import com.dreampany.radio.data.model.Station

/**
 * Created by roman on 31/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StationDataSource {
    @Throws
    suspend fun write(input: Station): Long

    @Throws
    suspend fun write(inputs: List<Station>): List<Long>?

    @Throws
    suspend fun reads(): List<Station>?

    @Throws
    suspend fun readsByCountry(
        countryCode: String,
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsByTop(
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsByTrend(
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsByPopular(
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun searchByName(
        name: String,
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun searchByTag(
        tag: String,
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun searchByLanguage(
        language: String,
        order: Station.Order,
        offset: Long,
        limit: Long
    ): List<Station>?
}