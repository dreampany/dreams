package com.dreampany.frame.util

import androidx.core.util.PatternsCompat
import com.dreampany.frame.misc.Constants
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Roman-372 on 7/25/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DataUtilKt {
    companion object {
        fun join(vararg items: Int): String {
            val builder = StringBuilder()
            for (item in items) {
                builder.append(item)
            }
            return builder.toString()
        }

        fun join(vararg items: String): String {
            val builder = StringBuilder()
            for (item in items) {
                builder.append(item)
            }
            return builder.toString()
        }

        fun getFirstPart(value: String, sep: String): String {
            return value.split(sep).first()
        }

        fun isValidUrl(url: String): Boolean {
            return PatternsCompat.WEB_URL.matcher(url).matches()
        }

        fun isValidImageUrl(url: String): Boolean {
            val pattern = Constants.Pattern.IMAGE_PATTERN
            return pattern.matcher(url).matches()
        }

        fun joinPrefixIf(url: String, prefix: String): String {
            return if (!url.startsWith(prefix)) prefix.plus(url) else url
        }

        fun formatReadableSize(value: Long): String {
            return formatReadableSize(value, false)
        }

        fun formatReadableSize(value: Long, si: Boolean): String {
            val unit = if (si) 1000 else 1024
            if (value < unit) return "$value B"
            val exp = (Math.log(value.toDouble()) / Math.log(unit.toDouble())).toInt()
            val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else ""
            return String.format(
                Locale.ENGLISH,
                "%.1f %sB",
                value / Math.pow(unit.toDouble(), exp.toDouble()),
                pre
            )
        }

/*        fun formatReadableSize(value: Long, si: Boolean): String {
            val unit = if (si) 1000 else 1024
            if (value < unit) return value.toString()
            val exp = (Math.log(value.toDouble()) / Math.log(unit.toDouble())).toInt()
            val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
            return String.format(
                Locale.ENGLISH,
                "%.1f %s",
                value / Math.pow(unit.toDouble(), exp.toDouble()),
                pre
            )
        }*/
    }
}