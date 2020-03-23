package com.dreampany.common.misc.extension

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.util.Util

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Boolean?.value(): Boolean = this ?: false

fun Int?.value(): Int = this ?: 0

fun Long?.value(): Long = this ?: 0L
fun String?.value(): String = this ?: Constants.Default.STRING

fun Int.isZeroOrLess(): Boolean {
    return this <= 0
}

fun Long.isExpired(delay: Long) : Boolean {
    return Util.currentMillis() - this > delay
}

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
fun Int.toColor(context: Context): Int {
    return ContextCompat.getColor(context, this)
}

fun String.toColor(): Int {
    return Color.parseColor(this)
}