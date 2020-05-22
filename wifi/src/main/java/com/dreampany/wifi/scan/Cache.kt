package com.dreampany.wifi.scan

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.dreampany.wifi.misc.Config
import org.apache.commons.lang3.builder.CompareToBuilder
import java.util.*

/**
 * Created by roman on 22/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Cache(
    private val config: Config
) {
    private val ADJUST = 10
    private val cacheResults: Deque<List<ScanResult>> = ArrayDeque<List<ScanResult>>()
    var cacheWifi: WifiInfo? = null

    val first : List<ScanResult>
        get() = cacheResults.first

    val last : List<ScanResult>
        get() = cacheResults.last

    val scanResults: List<CacheResult>
        get() {
            var current: ScanResult? = null
            var levelTotal = 0
            var count = 0
            val result = arrayListOf<CacheResult>()
            combineCache.forEach { scanResult ->
                current?.let {
                    if (!scanResult.BSSID.equals(it.BSSID)) {
                        val cache = getCacheResult(it, levelTotal, count)
                        result.add(cache)
                        count = 0
                        levelTotal = 0
                    }
                }
                current = scanResult
                count++
                levelTotal += scanResult.level
            }
            current?.let {
                result.add(getCacheResult(it, levelTotal, count))
            }
            return result
        }

    val cacheSize: Int
        get() {
            if (config.isSizeAvailable) {
                val scanSpeed = 5
                if (scanSpeed < 2) {
                    return 4
                }
                if (scanSpeed < 5) {
                    return 3
                }
                if (scanSpeed < 10) {
                    return 2
                }
            }
            return 1
        }

    fun add(result: List<ScanResult>, wifiInfo: WifiInfo?) {
        val size = cacheSize
        while (cacheResults.size >= size)
            cacheResults.pollLast()
        cacheResults.addFirst(result)
        cacheWifi = wifiInfo
    }

    private val combineCache: List<ScanResult>
        get() {
            val result = arrayListOf<ScanResult>()
            cacheResults.forEach { result.addAll(it) }
            Collections.sort(result, ScanResultComparator())
            return result
        }

    private fun getCacheResult(current: ScanResult, level: Int, count: Int): CacheResult {
        return if (config.isSizeAvailable) CacheResult(current, level / count)
        else CacheResult(current, (level - ADJUST) / count)
    }

    inner class ScanResultComparator : Comparator<ScanResult> {
        override fun compare(left: ScanResult, right: ScanResult): Int {
            return CompareToBuilder()
                .append(left.BSSID, right.BSSID)
                .append(left.level, right.level)
                .toComparison()
        }
    }
}