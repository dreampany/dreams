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
    suspend fun readsByTopClick(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsByTopVote(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsByLastClick(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun readsByLastChange(
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