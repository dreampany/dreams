package com.dreampany.wifi.scan

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import java.util.*

/**
 * Created by roman on 22/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Cache {
    private val ADJUST = 10
    private val cacheResults: Deque<List<ScanResult>> = ArrayDeque<List<ScanResult>>()
    private var cacheWifi: WifiInfo? = null

    /*val scanResults: List<ScanResult>
        get() {
            var current: ScanResult? = null
            var
        }

    private fun getCacheResult(current: ScanResult, level: Int, count: Int) : CacheResult {
        var cacheResult : CacheResult
        if ()
        return cacheResult
    }*/

    fun add(result: List<ScanResult>, wifiInfo: WifiInfo?) {
        cacheResults.addFirst(result)
        cacheWifi = wifiInfo
    }
}