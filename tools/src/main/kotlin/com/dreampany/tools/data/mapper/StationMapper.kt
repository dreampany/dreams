package com.dreampany.tools.data.mapper

import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.misc.Mapper
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.util.TimeUtil
import com.dreampany.tools.api.radio.RadioStation
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.pref.RadioPref
import com.dreampany.tools.injector.annote.StationAnnote
import com.dreampany.tools.injector.annote.StationItemAnnote
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.StationItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StationMapper
@Inject constructor(
    @StationAnnote private val map: SmartMap<String, Station>,
    @StationAnnote private val cache: SmartCache<String, Station>,
    @StationItemAnnote private val uiMap: SmartMap<String, StationItem>,
    @StationItemAnnote private val uiCache: SmartCache<String, StationItem>,
    private val pref: RadioPref
) : Mapper() {

    fun isExpired(state: State): Boolean {
        val lastTime = pref.getStationTime(state)
        return TimeUtil.isExpired(lastTime, Constants.Time.STATION)
    }

    fun isExpired(state: State, countryCode: String): Boolean {
        val lastTime = pref.getStationTime(state, countryCode)
        return TimeUtil.isExpired(lastTime, Constants.Time.STATION)
    }

    fun commitStationExpiredTime(state: State) {
        pref.commitStationTime(state)
    }

    fun commitStationExpiredTime(state: State, countryCode: String) {
        pref.commitStationTime(state, countryCode)
    }

    fun isExists(item: Station): Boolean {
        return map.contains(item.id)
    }

    fun getUiItem(id: String): StationItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: StationItem) {
        uiMap.put(id, uiItem)
    }

    fun getItems(inputs: List<RadioStation>?): List<Station>? {
        if (inputs.isNullOrEmpty()) return null
        val result = arrayListOf<Station>()
        inputs.forEach { rs ->
            result.add(getItem(rs))
        }
        return result
    }

    fun getItem(input: RadioStation): Station {
        var out: Station? = map.get(input.id)
        if (out == null) {
            out = Station(input.id)
            map.put(input.id, out)
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
}