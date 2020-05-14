package com.dreampany.network.data.model.wifi.band

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiChannels5GHZ : WifiChannels(range, channels) {
    companion object {
        private val range: Pair<Int, Int> = Pair(4900, 5899)
        private val set1 = Pair(WifiChannel(36, 5180), WifiChannel(64, 5320))
        private val set2 = Pair(WifiChannel(100, 5500), WifiChannel(144, 5720))
        private val set3 = Pair(WifiChannel(149, 5745), WifiChannel(165, 5825))
        private val channels: List<Pair<WifiChannel, WifiChannel>> = listOf(set1, set2, set3)
    }
}