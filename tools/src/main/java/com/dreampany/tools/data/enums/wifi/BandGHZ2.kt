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
class BandGHZ2 : BandGHZ(range, sets) {

    companion object {
        private val range = Pair<Int, Int>(2400, 2499)
        private val set1 = Pair<Channel, Channel>(Channel(1, 2412), Channel(13, 2472))
        private val set2 = Pair<Channel, Channel>(Channel(14, 2484), Channel(14, 2484))
        private val sets = Arrays.asList(set1, set2)
        private val set = Pair<Channel, Channel>(sets.first().first, sets.last().second)
    }

}