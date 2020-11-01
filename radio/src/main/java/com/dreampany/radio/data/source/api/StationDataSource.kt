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
    suspend fun readsByCountry(countryCode: String): List<Station>?

    @Throws
    suspend fun readsByCountry(
        countryCode: String,
        hideBroken: Boolean,
        order: Station.Order,
        reverse: Boolean
    ): List<Station>?

    @Throws
    suspend fun readsByTrend(): List<Station>?

    @Throws
    suspend fun readsByPopular(): List<Station>?
}