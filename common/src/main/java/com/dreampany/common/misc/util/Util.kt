package com.dreampany.common.misc.util

import java.util.*

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
object Util {

    private val random = Random()

    fun currentMillis() : Long = System.currentTimeMillis()

    fun nextRand(upper: Int): Int {
        return if (upper <= 0) -1 else random.nextInt(upper)
    }

    fun nextRand(upper: Long): Long {
        return if (upper <= 0) -1 else random.nextInt(upper.toInt()).toLong()
    }

    fun nextRand(min: Int, max: Int): Int {
        return random.nextInt(max - min + 1) + min
    }
}