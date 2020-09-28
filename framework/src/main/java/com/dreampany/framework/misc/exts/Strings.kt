package com.dreampany.framework.misc.exts

import android.text.Spanned
import android.util.Patterns
import androidx.core.text.HtmlCompat
import com.dreampany.framework.misc.constant.Constant
import com.google.common.io.BaseEncoding
import java.util.regex.Pattern

/**
 * Created by roman on 3/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
/*fun String.hash256(): String = Hashing.sha256().newHasher()
    .putString(this, Charsets.UTF_8).hash().toString()*/

fun String?.string(): String = this ?: Constant.Default.STRING

fun String?.isEquals(value: String?): Boolean = this == value

fun String?.lastPart(denim: Char): String? = this?.split(denim)?.last()

fun String?.firstPart(denim: Char): String? = this?.split(denim)?.first()

//fun String?.isEmail(): Boolean = this?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() } ?: false

fun String.append(vararg suffixes : String) : String {
    val builder = StringBuilder(this)
    suffixes.forEach { builder.append(it) }
    return builder.toString()
}

fun String?.parseInt(): Int = this?.toInt() ?: 0

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

val CharSequence?.value: String
    get() = if (this == null) Constant.Default.STRING else this.toString()

val CharSequence?.trimValue: String
    get() = if (this == null) Constant.Default.STRING else this.trim().toString()

val String?.html: Spanned get() = HtmlCompat.fromHtml(this.string(), HtmlCompat.FROM_HTML_MODE_COMPACT)

fun join(vararg values: String) : String {
    val builder = StringBuilder()
    values.forEach { builder.append(it) }
    return builder.toString()
}

val String.hasHttpSign: Boolean
    get() {
        return this.startsWith("http")
    }

val String.hasDrawableSign: Boolean
    get() {
        return this.startsWith("@drawable/")
    }

val String.encodeBase64 : String
    get() = BaseEncoding.base64().encode(this.toByteArray(Charsets.UTF_8))

val String.decodeBase64 : String
    get() = BaseEncoding.base64().decode(this).toString(Charsets.UTF_8)

val String?.isEmail: Boolean
    get() = this?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() } ?: false

val String?.isPassword: Boolean
    get() = this?.let { Pattern.compile(Constant.Regex.PASSWORD).matcher(it).matches() } ?: false

val String?.isName: Boolean
    get() = this?.let { Pattern.compile(Constant.Regex.NAME).matcher(this).matches() } ?: false