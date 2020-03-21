package com.dreampany.common.misc.extension

import com.dreampany.common.misc.constant.Constants

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