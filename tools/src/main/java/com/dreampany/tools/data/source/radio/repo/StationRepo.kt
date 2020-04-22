package com.dreampany.tools.data.source.radio.repo

import com.dreampany.common.inject.annote.Remote
import com.dreampany.common.inject.annote.Room
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.data.source.radio.api.StationDataSource
import com.dreampany.tools.data.source.radio.mapper.StationMapper
import com.dreampany.tools.data.source.radio.pref.RadioPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StationRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: RadioPref,
    private val mapper: StationMapper,
    @Room private val room: StationDataSource,
    @Remote private val remote: StationDataSource
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
    ) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(RadioState.LOCAL, countryCode, hideBroken, order, reverse, offset)) {
            val result =
                remote.getItemsOfCountry(countryCode, hideBroken, order, reverse, offset, limit)
            if (!result.isNullOrEmpty()) {
                mapper.commitExpire(RadioState.LOCAL, countryCode, hideBroken, order, reverse, offset)
                room.putItems(result)
            }
        }
        room.getItemsOfCountry(countryCode, hideBroken, order, reverse, offset, limit)
    }

    @Throws
    override suspend fun getItemsOfTrends(limit: Long) = withContext(Dispatchers.IO) {
        /*if (mapper.isExpired(RadioState.TRENDS)) {
            val result = remote.getItemsOfTrends(limit)
            if (!result.isNullOrEmpty()) {
                mapper.commitExpire(RadioState.TRENDS)
                room.putItems(result)
            }
        }*/
        remote.getItemsOfTrends(limit)
    }

    @Throws
    override suspend fun getItemsOfPopular(limit: Long) = withContext(Dispatchers.IO) {
        /*if (mapper.isExpired(RadioState.POPULAR)) {
            val result = remote.getItemsOfPopular(limit)
            if (!result.isNullOrEmpty()) {
                mapper.commitExpire(RadioState.POPULAR)
                room.putItems(result)
            }
        }*/
        remote.getItemsOfPopular(limit)
    }
}