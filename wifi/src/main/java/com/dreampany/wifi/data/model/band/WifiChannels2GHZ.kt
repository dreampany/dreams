package com.dreampany.wifi.data.model.band

import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
class WifiChannels2GHZ : WifiChannels(range, channels) {
    companion object {
        private val range: Pair<Int, Int> = Pair(2400, 2499)
        private val set1 = Pair(WifiChannel(1, 2412), WifiChannel(13, 2472))
        private val set2 = Pair(WifiChannel(14, 2484), WifiChannel(14, 2484))
        private val channels: List<Pair<WifiChannel, WifiChannel>> = listOf(set1, set2)
        private val channel: Pair<WifiChannel, WifiChannel> =
            Pair(channels.first().first, channels.last().second)
    }
}