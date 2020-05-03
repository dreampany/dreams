package com.dreampany.framework.misc.extension

import android.os.Build

/**
 * Created by roman on 3/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
val Any?.isQ: Boolean
    get() {
        if (this == null) return false
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

val Any?.isQ: Boolean
    get() {
        if (this == null) return false
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }