package com.dreampany.tools.data.source.radio.mapper

import com.dreampany.common.misc.extension.isExpired
import com.dreampany.tools.api.radio.RadioStation
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.data.source.radio.pref.RadioPref
import com.dreampany.tools.misc.constant.AppConstants
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

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
        state: RadioState, order: StationOrder, reverse: Boolean,
        offset: Long
    ): Boolean {
        val time = pref.getExpireTime(state, order, reverse, offset)
        return time.isExpired(AppConstants.Times.RADIO.LISTING)
    }

    @Synchronized
    fun isExpired(
        state: RadioState,
        countryCode: String,
        order: StationOrder,
        reverse: Boolean,
        offset: Long
    ): Boolean {
        val time = pref.getExpireTime(state, countryCode, order, reverse, offset)
        return time.isExpired(AppConstants.Times.RADIO.LISTING)
    }

    @Synchronized
    fun commitExpire(
        state: RadioState
    ) = pref.commitExpireTime(state)

    @Synchronized
    fun commitExpire(
        state: RadioState, order: StationOrder,
        reverse: Boolean,
        offset: Long
    ) = pref.commitExpireTime(state, order, reverse, offset)

    @Synchronized
    fun commitExpire(
        state: RadioState, countryCode: String, order: StationOrder,
        reverse: Boolean,
        offset: Long
    ) = pref.commitExpireTime(state, countryCode, order, reverse, offset)


    @Synchronized
    fun add(station: Station) = stations.put(station.id, station)

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
}