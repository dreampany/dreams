package com.dreampany.radio.data.source.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.data.source.api.StationDataSource
import com.dreampany.radio.data.source.mapper.StationMapper
import com.dreampany.radio.data.source.pref.Prefs
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StationRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: Prefs,
    private val mapper: StationMapper,
    @Remote private val remote: StationDataSource
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

    override suspend fun readsByTopClick(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsByTopVote(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsByLastClick(
        order: String,
        offset: Long,
        limit: Long
    ): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun readsByLastChange(
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