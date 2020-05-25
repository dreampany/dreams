package com.dreampany.framework.misc.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.constant.Constants
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

    fun isMinO(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    fun <T : Context> createIntent(source: T, target: Class<*>, task: Task<*,*,*, *, *>?): Intent {
        val intent = Intent(source, target)
        if (task != null) {
            intent.putExtra(Constants.Keys.TASK, task as Parcelable?)
        }
        return intent
    }

    fun sleep(time: Long): Boolean {
        try {
            Thread.sleep(time)
            return true
        } catch (ignored: InterruptedException) {
            return false
        }
    }
}