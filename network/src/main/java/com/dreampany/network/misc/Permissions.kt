package com.dreampany.network.misc

import android.Manifest.permission.*
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

val isMinLollipop: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

val isMinM: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

val isMinN: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

val isMinO: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

val isMinQ: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun Context?.hasManifest(permission: String): Boolean =
    packageInfo(packageName, PackageManager.GET_PERMISSIONS)?.requestedPermissions?.contains(
        permission
    ) ?: false

fun Context?.hasPermission(permission: String): Boolean =
    if (isMinM) selfPermission(permission)
    else hasManifest(permission)

fun Context?.selfPermission(permission: String): Boolean =
    if (this == null) false
    else ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


val Context?.hasOverlayPermission: Boolean
    @RequiresApi(Build.VERSION_CODES.M)
    get() = if (isMinM) {
        Settings.canDrawOverlays(this)
    } else {
        hasManifest(SYSTEM_ALERT_WINDOW)
    }


val Context?.hasUsagePermission: Boolean
    get() {
        if (isMinLollipop) {
            val appOps =
                this?.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager? ?: return false
            val pkg = packageName ?: return false
            val mode = appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), pkg
            )
            return mode == MODE_ALLOWED
        } else {
            return if (isMinM) {
                hasManifest(PACKAGE_USAGE_STATS)
            } else {
                hasManifest(GET_TASKS)
            }
        }
    }

val Context?.hasLocationPermission: Boolean
    get() {
        if (isMinM) {
            val location = locationManager ?: return false
            return LocationManagerCompat.isLocationEnabled(location)
        } else {
            return hasManifest(ACCESS_FINE_LOCATION) || hasManifest(ACCESS_COARSE_LOCATION)
        }
    }

