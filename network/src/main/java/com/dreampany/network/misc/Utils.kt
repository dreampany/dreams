package com.dreampany.network.misc

import java.util.*

/**
 * Created by roman on 20/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Utils {



    companion object {
        private val random = Random()
        fun nextRand(upper: Int): Int = if (upper <= 0) -1 else random.nextInt(upper)

        fun nextRand(min: Int, max: Int): Int = random.nextInt(max - min + 1) + min
    }
}