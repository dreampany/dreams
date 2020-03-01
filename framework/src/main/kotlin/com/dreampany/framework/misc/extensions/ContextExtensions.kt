package com.dreampany.framework.misc.extensions

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

/**
 * Created by roman on 1/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun Context?.isDebug(): Boolean {
    if (this == null) return true
    var debug = false
    try {
        val appInfo = this.applicationContext.packageManager.getApplicationInfo(
            this.applicationContext.getPackageName(), 0
        )
        debug = (0 != (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
    } catch (ignored: PackageManager.NameNotFoundException) {
        /*debuggable variable will remain false*/
    }
    return debug
}

fun Context?.isRelease(): Boolean {
    return !this.isDebug()
}