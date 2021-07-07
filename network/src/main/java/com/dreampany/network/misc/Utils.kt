package com.dreampany.network.misc

import kotlin.random.Random

/**
 * Created by roman on 7/8/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Utils {

    companion object {
        fun nextRand(upper: Int): Int = if (upper <= 0) -1 else Random.nextInt(upper)

        fun nextRand(min: Int, max: Int): Int = Random.nextInt(max - min + 1) + min

    }
}