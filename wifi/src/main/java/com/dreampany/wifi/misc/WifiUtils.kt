package com.dreampany.wifi.misc

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiUtils {
    companion object {
        private const val MIN_RSSI = -100
        private const val MAX_RSSI = -55

        //fun calculateDistance

        fun calculateSignalLevel(rssi: Int, levels: Int): Int {
            if (rssi <= MIN_RSSI) return 0
            if (rssi >= MAX_RSSI) return levels.dec()
            return (rssi - MIN_RSSI) * levels.dec() / (MAX_RSSI - MIN_RSSI)
        }
    }
}