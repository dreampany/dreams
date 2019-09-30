package com.dreampany.framework.util

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

        fun resolve(text: String? = null): String {
            if (text.isNullOrEmpty()) Constants.Default.STRING
            return text!!
        }
    }
}