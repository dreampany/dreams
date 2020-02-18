package com.dreampany.framework.misc.extension

import android.text.Editable
import com.dreampany.framework.misc.Constants

/**
 * Created by roman on 2019-10-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun String.toTitle(): String {
    var space = true
    val builder = StringBuilder(this)
    val len = builder.length

    for (i in 0 until len) {
        val c = builder[i]
        if (space) {
            if (!Character.isWhitespace(c)) {
                // Convert to setTitle case and switch out of whitespace mode.
                builder.setCharAt(i, Character.toTitleCase(c))
                space = false
            }
        } else if (Character.isWhitespace(c)) {
            space = true
        } else {
            builder.setCharAt(i, Character.toLowerCase(c))
        }
    }

    return builder.toString()
}

fun String?.string(): String {
    return if (this == null) Constants.Default.STRING else this
}

fun String?.isEqual(instance: String?): Boolean {
    if (this == instance) return true
    if (this == null || instance == null) return false
    return this.equals(instance)
}

fun String?.isEqual(instance: CharSequence?): Boolean {
    if (this == instance) return true
    if (this == null || instance == null) return false
    return this.equals(instance)
}

fun String?.isEqual(instance: Editable?): Boolean {
    if (this != null) return this.equals(instance)
    return instance!!.equals(this)
}

fun CharSequence?.isEqual(instance: CharSequence?): Boolean {
    if (this == instance) return true
    if (this == null || instance == null) return false
    return this.equals(instance)
}

fun String?.parseInt() : Int {
    if (this.isNullOrEmpty()) return 0
    return this.toInt()
}

fun String?.parseLong() : Long {
    if (this.isNullOrEmpty()) return 0L
    return this.toLong()
}

/*fun String.join(vararg items: String, sep: String): String {
    val builder = StringBuilder()
    for (item in items) {
        if (builder.isNotEmpty()) builder.append(sep)
        builder.append(item)
    }
    return builder.toString()
}*/

