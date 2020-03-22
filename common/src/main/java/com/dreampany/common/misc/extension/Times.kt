package com.dreampany.common.misc.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by roman on 3/22/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
private val UTC_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

fun String.utc(): Long {
    val format = SimpleDateFormat(UTC_PATTERN, Locale.getDefault())
    return format.parse(this)?.time ?: 0L
}