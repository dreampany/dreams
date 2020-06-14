package com.dreampany.tools.data.source.radio.mapper

import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.framework.misc.exts.sub
import com.dreampany.tools.api.radio.RadioStation
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.data.source.radio.api.StationDataSource
import com.dreampany.tools.data.source.radio.pref.RadioPref
import com.dreampany.tools.misc.constants.AppConstants
import com.google.common.collect.Maps
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableMap
import kotlin.collections.arrayListOf
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.sortWith

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StationMapper
@Inject constructor(
    private val pref: RadioPref
) {
    private val stations: MutableMap<String, Station>

    init {
        stations = Maps.newConcurrentMap()
    }

    @Synchronized
    fun isExpired(
        state: RadioState
    ): Boolean {
        val time = pref.getExpireTime(state)
        return time.isExpired(AppConstants.Times.RADIO.LISTING)
    }

    @Synchronized
    fun isExpired(
        state: RadioState,
        countryCode: String,
        hideBroken: Boolean,
        order: StationOrder,
        reverse: Boolean,
        offset: Long
    ): Boolean {
        val time = pref.getExpireTime(state, countryCode, hideBroken, order, reverse, offset)
        return time.isExpired(AppConstants.Times.RADIO.LISTING)
    }

    @Synchronized
    fun commitExpire(
        state: RadioState
    ) = pref.commitExpireTime(state)

    @Synchronized
    fun commitExpire(
        state: RadioState, countryCode: String, hideBroken: Boolean, order: StationOrder,
        reverse: Boolean,
        offset: Long
    ) = pref.commitExpireTime(state, countryCode, hideBroken, order, reverse, offset)


    @Synchronized
    fun add(station: Station) = stations.put(station.id, station)


    @Throws
    @Synchronized
    suspend fun getItems(
        state: RadioState,
        countryCode: String,
        hideBroken: Boolean,
        order: StationOrder,
        reverse: Boolean,
        offset: Long,
        limit: Long,
        source: StationDataSource
    ): List<Station>? {
        val items = source.getItems(countryCode, limit) ?: return null
        val cache = sortedStations(items, order, reverse)
        val result = sub(cache, offset, limit)
        return result
    }

    @Synchronized
    fun getItems(inputs: List<RadioStation>): List<Station> {
        val result = arrayListOf<Station>()
        inputs.forEach { rs ->
            result.add(getItem(rs))
        }
        return result
    }

    @Synchronized
    fun getItem(input: RadioStation): Station {
        var out: Station? = stations.get(input.id)
        if (out == null) {
            out = Station(input.id)
            stations.put(input.id, out)
        }
        out.setChangeUuid(input.changeUuid)
        out.setStationUuid(input.stationUuid)
        out.name = input.name
        out.url = input.url
        out.homepage = input.homepage
        out.favicon = input.favicon
        out.ip = input.ip
        out.codec = input.codec
        out.bitrate = input.bitrate
        out.tags = input.tags
        out.country = input.country
        out.setCountryCode(input.countryCode)
        out.state = input.state
        out.language = input.language
        out.votes = input.votes
        out.setNegativeVotes(input.negativeVotes)
        out.setClickCount(input.clickCount)
        out.setClickTrend(input.clickTrend)
        out.setLastCheckOk(input.lastCheckOk != 0)

        return out
    }

    /*fun getItem(input: Store, source: StationDataSource): Station? {
        var out: Station? = map.get(input.id)
        if (out == null) {
            out = source.getItem(input.id)
            map.put(input.id, out)
        }
        return out
    }*/

    @Throws
    @Synchronized
    private suspend fun updateCache(source: StationDataSource) {
        if (stations.isEmpty()) {
            source.getItems()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sortedStations(
        items : List<Station>,
        order: StationOrder,
        reverse: Boolean
    ): List<Station> {
        val temp = ArrayList(items)
        val comparator = StationComparator(order, reverse)
        temp.sortWith(comparator)
        return temp
    }

    class StationComparator(
        private val order: StationOrder,
        private val reverse: Boolean
    ) : Comparator<Station> {
        override fun compare(left: Station, right: Station): Int {
            if (order == StationOrder.NAME) {
                val leftName = left.name
                val rightName = right.name
                if (leftName != null && rightName != null) {
                    if (reverse) {
                        return rightName.compareTo(leftName)
                    } else {
                        return leftName.compareTo(rightName)
                    }
                }
            }
            return 0
        }
    }
}