package com.dreampany.framework.misc.extension

import android.graphics.Color
import androidx.annotation.ColorInt
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.util.Util
import java.util.*

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Boolean?.value(): Boolean = this ?: false

fun Int?.value(): Int = this ?: 0

fun Long?.value(): Long = this ?: 0L
fun Double?.value(): Double = this ?: 0.toDouble()

fun String?.value(): String = this ?: Constants.Default.STRING

fun Int.isZeroOrLess(): Boolean = this <= 0

fun Long.isExpired(delay: Long): Boolean = Util.currentMillis() - this > delay

fun <T> sub(list: List<T>?, index: Long, limit: Long): List<T>? {
    var limit = limit
    if (list.isNullOrEmpty() || index < 0 || limit < 1 || list.size <= index) {
        return null
    }
    if (list.size - index < limit) {
        limit = list.size - index
    }
    return list.subList(index.toInt(), (index + limit).toInt())
}

@ColorInt
fun String.toColor(): Int = Color.parseColor(this)

fun randomId(): String = UUID.randomUUID().toString()