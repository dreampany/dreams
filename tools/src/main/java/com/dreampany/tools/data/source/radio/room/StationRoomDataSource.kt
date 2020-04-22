package com.dreampany.tools.data.source.radio.room

import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.data.source.radio.api.StationDataSource
import com.dreampany.tools.data.source.radio.mapper.StationMapper
import com.dreampany.tools.data.source.radio.room.dao.StationDao

/**
 * Created by roman on 21/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationRoomDataSource(
    private val mapper: StationMapper,
    private val dao: StationDao
) : StationDataSource {

    @Throws
    override suspend fun putItem(item: Station): Long {
        mapper.add(item)
        return dao.insertOrReplace(item)
    }

    @Throws
    override suspend fun putItems(items: List<Station>): List<Long>? {
        val result = arrayListOf<Long>()
        items.forEach { result.add(putItem(it)) }
        return result
    }

    @Throws
    override suspend fun getItems(): List<Station>? = dao.items

    override suspend fun getItems(countryCode: String): List<Station>? = dao.getItems(countryCode)

    override suspend fun getItemsOfCountry(
        countryCode: String,
        hideBroken: Boolean,
        order: StationOrder,
        reverse: Boolean,
        offset: Long,
        limit: Long
    ): List<Station>? = mapper.getItems(RadioState.LOCAL, countryCode, hideBroken, order, reverse, offset, limit, this)

    override suspend fun getItemsOfTrends(limit: Long): List<Station>? {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsOfPopular(limit: Long): List<Station>? {
        TODO("Not yet implemented")
    }
}