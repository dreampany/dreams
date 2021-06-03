package com.dreampany.common.misc.exts

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

/**
 * Created by roman on 6/3/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
val Context?.isDebug: Boolean
    get() {
        if (this == null) return true
        var debug = true
        try {
            val appInfo = this.applicationContext.packageManager.getApplicationInfo(
                this.applicationContext.getPackageName(), 0
            )
            debug = (0 != (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
        } catch (ignored: PackageManager.NameNotFoundException) {
        }
        return debug
    }