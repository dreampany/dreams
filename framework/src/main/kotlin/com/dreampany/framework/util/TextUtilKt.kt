package com.dreampany.framework.util

import android.content.Context
import androidx.annotation.StringRes
import com.dreampany.framework.misc.Constants

/**
 * Created by roman on 2019-08-04
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TextUtilKt {

    companion object {
        fun getFirst(value: String): String {
            return value.first().toString()
        }

        fun takeFirst(value: String, len: Int): String {
            return value.take(len)
        }

        fun resolve(text: String? = null): String {
            if (text.isNullOrEmpty()) Constants.Default.STRING
            return text!!
        }

        fun getString(context: Context, @StringRes resId: Int): String {
            return context.getString(resId)
        }

        fun getStrings(context: Context, vararg resourceIds: Int): Array<String> {
            val list = arrayListOf<String>()
            for (index in resourceIds.indices) {
                list.add(getString(context, resourceIds[index]))
            }
            return list.toTypedArray()
        }
    }
}