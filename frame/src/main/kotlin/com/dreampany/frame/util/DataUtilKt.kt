package com.dreampany.frame.util

import androidx.core.util.PatternsCompat
import com.dreampany.frame.misc.Constants
import com.google.common.base.Strings
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Roman-372 on 7/25/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DataUtilKt {
    companion object {

        fun getRandId() :String {
            return UUID.randomUUID().toString()
        }

        fun isEmpty(vararg items: String): Boolean {
            if (items.isEmpty()) {
                return true
            }
            for (item in items) {
                if (!Strings.isNullOrEmpty(item)) {
                    return false
                }
            }
            return true
        }

        fun isAnyEmpty(vararg items: String?): Boolean {
            if (items.isEmpty()) {
                return true
            }
            for (item in items) {
                if (Strings.isNullOrEmpty(item)) {
                    return true
                }
            }
            return false
        }

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

/*        fun isEmpty(collection: Collection<*>?): Boolean {
            return collection == null || collection.isEmpty()
        }*/

        fun <T> sub(list: MutableList<T>?, count: Int): MutableList<T>? {
            var count = count
            if (list.isNullOrEmpty()) {
                return null
            }
            count = if (list.size < count) list.size else count
            return ArrayList(list.subList(0, count))
        }

        fun <T> takeFirst(list: MutableList<T>?, count: Int): MutableList<T>? {
            if (list.isNullOrEmpty()) {
                return null
            }

            val result = sub(list, count)
            removeAll(list, result)
            return result
        }

        fun <T> removeAll(list: MutableList<T>, sub: MutableList<T>?): MutableList<T>? {
            sub?.run {
                list.removeAll(this)
            }
            return list
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