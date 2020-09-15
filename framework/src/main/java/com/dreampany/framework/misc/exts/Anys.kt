package com.dreampany.framework.misc.exts

import android.graphics.Color
import androidx.annotation.ColorInt
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.util.Util
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
val Boolean.default: Boolean get() = false
val Int.default get() = 0
val Long.default get() = 0L
val Float.default get() = 0f
val Double.default get() = 0.0
val String.default get() = ""

fun boolean() = false
fun int() = 0
fun long() = 0L
fun float() = 0f
fun double() = 0.0
fun string() = ""

val Boolean?.value: Boolean get() = this ?: false

val Int?.value: Int get() = this ?: 0

val Long?.value: Long get() = this ?: 0L

val Double?.value: Double get() = this ?: 0.toDouble()

val String?.value: String get() = this ?: Constants.Default.STRING

val Int.isZeroOrLess: Boolean get() = this <= 0

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

fun append(vararg values: Any): String {
    val builder = StringBuilder()
    values.forEach { builder.append(it) }
    return builder.toString()
}

val ByteArray?.isEmpty: Boolean
    get() {
        if (this == null) return true
        if (this.size == 0) return true
        return false
    }

val ByteArray?.length: Int
    get() = this?.size ?: 0

/*val Long.count: String
    get() = String.format(Locale.getDefault(), "%,d", this)*/

val Long.count: String
    get() {
        val suffixChars = "KMGTPE"
        val formatter = DecimalFormat("###.#")
        formatter.roundingMode = RoundingMode.DOWN
        return if (this < 1000.0) {
            formatter.format(this)
        } else {
            val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
            formatter.format(this / 1000.0.pow(exp.toDouble())) + suffixChars[exp - 1]
        }
    }

