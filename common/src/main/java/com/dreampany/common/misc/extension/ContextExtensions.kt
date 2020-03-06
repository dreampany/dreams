package com.dreampany.common.misc.extension

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Context?.dimension(@DimenRes resId: Int): Float {
    return this?.resources?.getDimension(resId) ?: 0.0f
}

fun Context?.color(@ColorRes resId: Int): Int {
    return if (this == null) 0 else ContextCompat.getColor(this, resId)
}