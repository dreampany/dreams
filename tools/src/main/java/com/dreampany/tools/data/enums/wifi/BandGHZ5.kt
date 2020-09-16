package com.dreampany.tools.data.enums.wifi

import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by roman on 16/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
class BandGHZ5 : BandGHZ(range, sets) {

    companion object {
        private val range = Pair<Int, Int>(4900, 5899)
        private val set1 = Pair<Channel, Channel>(Channel(36, 5180), Channel(64, 5320))
        private val set2 = Pair<Channel, Channel>(Channel(100, 5500), Channel(144, 5720))
        private val set3 = Pair<Channel, Channel>(Channel(149, 5745), Channel(165, 5825))
        private val sets = Arrays.asList(set1, set2, set3)
    }

}