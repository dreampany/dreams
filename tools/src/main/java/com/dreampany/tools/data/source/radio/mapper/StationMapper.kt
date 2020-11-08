package com.dreampany.tools.data.source.radio.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.tools.api.radiobrowser.RadioStation
import com.dreampany.tools.data.model.radio.Page
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.data.source.radio.pref.Prefs
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import com.google.gson.Gson
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
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val timeRepo: TimeRepo,
    private val pref: Prefs,
    private val gson: Gson
) {

    private val stations: MutableMap<String, Station>
    private val favorites: MutableMap<String, Boolean>

    init {
        stations = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    fun isExpired(type: Page.Type): Boolean {
        val time = pref.readExpireTime(type)
        return time.isExpired(Constants.Times.Radio.STATIONS)
    }

    @Synchronized
    fun read(input: RadioStation): Station {
        var out: Station? = stations.get(input.id)
        if (out == null) {
            out = Station(input.id)
            stations.put(input.id, out)
        }
        out.setChangeUuid(input.changeUuid)
        out.name = input.name
        out.url = input.url
        out.setUrlResolved(input.urlResolved)
        out.homepage = input.homepage
        out.favicon = input.favicon
        out.tags = input.tags?.split(Constant.Sep.COMMA)?.map { it.trim() }
        out.country = input.country
        out.setCountryCode(input.countryCode)
        out.state = input.state
        out.languages = input.language?.split(Constant.Sep.COMMA)?.map { it.trim() }
        out.votes = input.votes

        out.codecs = input.codec?.split(Constant.Sep.COMMA)?.map { it.trim() }
        out.bitrate = input.bitrate
        out.hls = input.hls != 0

        out.setLastCheckOk(input.lastCheckOk != 0)

        out.setClickCount(input.clickCount)
        out.setClickTrend(input.clickTrend)

        return out
    }

    @Synchronized
    fun reads(inputs: List<RadioStation>): List<Station> {
        return inputs.map { read(it) }
    }
}