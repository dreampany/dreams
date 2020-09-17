package com.dreampany.tools.misc.utils

import kotlin.math.abs

/**
 * Created by roman on 14/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiUtils {
    companion object {
        private const val DISTANCE_MHZ_M = 27.55
        private const val MIN_RSSI = -100
        private const val MAX_RSSI = -55
        private const val QUOTE = "\""

        fun calculateDistance(frequency: Int, level: Int): Double {
            return Math.pow(
                10.0,
                (DISTANCE_MHZ_M - (20 * Math.log10(frequency.toDouble())) + abs(level)) / 20.0
            )
        }

        fun calculateSignalLevel(rssi: Int, levels: Int): Int {
            if (rssi <= MIN_RSSI) return 0
            if (rssi >= MAX_RSSI) return levels.dec()
            return (rssi - MIN_RSSI) * levels.dec() / (MAX_RSSI - MIN_RSSI)
        }
    }
}