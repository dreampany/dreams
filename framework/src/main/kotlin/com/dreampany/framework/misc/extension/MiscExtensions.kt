package com.dreampany.framework.misc.extension

import android.content.Context

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun Int.resToStringArray(context: Context?): Array<String>? {
    //if (context == null) return null
    return context?.resources?.getStringArray(this)
}