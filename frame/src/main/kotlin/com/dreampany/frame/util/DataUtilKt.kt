package com.dreampany.frame.util

import androidx.core.util.PatternsCompat

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
    }
}