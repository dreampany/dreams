package com.dreampany.framework.misc.extension

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by roman on 3/22/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
private val UTC_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

fun String.utc(): Long {
    val format = SimpleDateFormat(UTC_PATTERN, Locale.getDefault())
    try {
        return format.parse(this)?.time ?: 0L
    } catch (error: ParseException) {
        Timber.e(error)
        return 0L
    }
}

fun Long.format(pattern: String) : String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val format = SimpleDateFormat(pattern, Locale.getDefault())
   return format.format(calendar.time)
}

