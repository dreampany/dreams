package com.dreampany.common.misc.extension

import android.Manifest.permission.*
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.provider.Settings

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun Context?.hasManifest(permission: String): Boolean {
    val info = packageInfo(packageName(), PackageManager.GET_PERMISSIONS)
    return info?.requestedPermissions?.contains(permission) ?: false
}

fun Context?.hasOverlayPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(this)
    } else {
        hasManifest(SYSTEM_ALERT_WINDOW)
    }
}


fun Context?.hasUsagePermission(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val appOps =
            this?.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager? ?: return false
        val pkg = packageName() ?: return false
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), pkg
        )
        return mode == MODE_ALLOWED
    } else {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasManifest(PACKAGE_USAGE_STATS)
        } else {
            hasManifest(GET_TASKS)
        }
    }
}