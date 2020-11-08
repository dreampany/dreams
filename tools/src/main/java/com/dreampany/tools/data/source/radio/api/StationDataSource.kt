package com.dreampany.tools.data.source.radio.api

import com.dreampany.tools.data.model.radio.Station

/**
 * Created by roman on 18/4/20
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
        country: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsByCountryCode(
        countryCode: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsByLanguage(
        language: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsTrend(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsPopular(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsRecent(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsChange(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun searchByName(
        name: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun searchByTag(
        tag: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?
}