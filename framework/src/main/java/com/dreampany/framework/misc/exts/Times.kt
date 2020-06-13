package com.dreampany.framework.misc.exts

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by roman on 3/22/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
private val UTC_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private val SIMPLE_UTC_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss'Z'"

val Calendar.day: Int get() = this.get(Calendar.DAY_OF_MONTH)
val Calendar.month: Int get() = this.get(Calendar.MONTH).inc()
val Calendar.year: Int get() = this.get(Calendar.YEAR)

val currentMillis: Long get() = System.currentTimeMillis()
fun currentDay(): Int = Calendar.getInstance().day
fun currentMonth(): Int = Calendar.getInstance().month
fun currentYear(): Int = Calendar.getInstance().year

val String.utc: Long
    get() {
        val format = SimpleDateFormat(UTC_PATTERN, Locale.getDefault())
        try {
            return format.parse(this)?.time ?: 0L
        } catch (error: ParseException) {
            Timber.e(error)
            return 0L
        }
    }

val String.simpleUtc: Long
    get() {
        val format = SimpleDateFormat(SIMPLE_UTC_PATTERN, Locale.getDefault())
        try {
            return format.parse(this)?.time ?: 0L
        } catch (error: ParseException) {
            Timber.e(error)
            return 0L
        }
    }

fun Long.format(pattern: String): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(calendar.time)
}

fun String.calendar(pattern: String): Calendar? {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    try {
        val date = format.parse(this) ?: return null
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.time
        return calendar
    } catch (error: ParseException) {
    }
    return null
}

fun String.getDay(pattern: String): Int = calendar(pattern)?.day ?: 0

fun String.getMonth(pattern: String): Int = calendar(pattern)?.month ?: 0

