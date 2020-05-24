package com.dreampany.tools.data.source.wifi.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.extension.sub
import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.data.source.wifi.api.WifiDataSource
import com.dreampany.tools.data.source.wifi.pref.WifiPref
import com.google.common.collect.Maps
import org.apache.commons.lang3.builder.CompareToBuilder
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableMap
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.sortWith
import kotlin.collections.toList

/**
 * Created by roman on 24/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WifiMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: WifiPref
) {
    private val wifis: MutableMap<String, Wifi>

    init {
        wifis = Maps.newConcurrentMap()
    }

    @Synchronized
    fun add(wifi: Wifi) = wifis.put(wifi.id, wifi)

    @Throws
    @Synchronized
    suspend fun gets(
        source: WifiDataSource,
        offset: Long,
        limit: Long,
        callback: () -> Unit
    ): List<Wifi>? {
        updateCache(source, callback)
        val cache = sortedWifis(wifis.values.toList())
        val result = sub(cache, offset, limit)
        return result
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: WifiDataSource, callback: () -> Unit) {
        if (wifis.isEmpty()) {
            source.gets(callback)?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sortedWifis(
        inputs: List<Wifi>
    ): List<Wifi> {
        val temp = ArrayList(inputs)
        val comparator = WifiComparator()
        temp.sortWith(comparator)
        return temp
    }

    class WifiComparator(
    ) : Comparator<Wifi> {
        override fun compare(left: Wifi, right: Wifi): Int = CompareToBuilder()
            .append(left.bssid, right.bssid)
            .append(left.level, right.level)
            .toComparison()
    }
}