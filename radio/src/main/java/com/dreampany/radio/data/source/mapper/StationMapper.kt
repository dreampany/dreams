package com.dreampany.radio.data.source.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.radio.data.model.Page
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.data.source.pref.Prefs
import com.dreampany.radio.misc.Constants
import com.google.common.collect.Maps
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/11/20
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
        return time.isExpired(Constants.Times.STATIONS)
    }
}