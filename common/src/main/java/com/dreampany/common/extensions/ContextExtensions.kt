package com.dreampany.common.extensions

import android.content.Context
import androidx.annotation.DimenRes

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Context?.dimension(@DimenRes resId: Int): Float {
    return this?.resources?.getDimension(resId) ?: 0.0f
}