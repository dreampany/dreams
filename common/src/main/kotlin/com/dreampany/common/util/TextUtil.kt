package com.dreampany.common.util

import android.content.Context
import androidx.annotation.StringRes

/**
 * Created by roman on 2019-05-20
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TextUtil private constructor() {
    companion object {
        fun getString(context: Context, @StringRes resId: Int): String {
            return context.getString(resId)
        }

        fun getString(context: Context, @StringRes resId: Int, vararg args: Any): String {
            return context.getString(resId, *args)
        }
    }

}