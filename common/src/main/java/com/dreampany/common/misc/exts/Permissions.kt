package com.dreampany.common.misc.exts

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
val isMinL: Boolean
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
        hasManifest(Manifest.permission.SYSTEM_ALERT_WINDOW)
    }


val Context?.hasUsagePermission: Boolean
    get() {
        if (isMinL) {
            val appOps =
                this?.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager? ?: return false
            val pkg = packageName ?: return false
            val mode = appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), pkg
            )
            return mode == AppOpsManager.MODE_ALLOWED
        } else {
            return if (isMinM) {
                hasManifest(Manifest.permission.PACKAGE_USAGE_STATS)
            } else {
                hasManifest(Manifest.permission.GET_TASKS)
            }
        }
    }

val Context?.hasLocationPermission: Boolean
    get() = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)

/*val Context?.hasLocationPermission: Boolean
    get() {
        if (isMinM) {
            val location = locationManager ?: return false
            return LocationManagerCompat.isLocationEnabled(location)
        } else {
           return hasManifest(ACCESS_FINE_LOCATION) || hasManifest(ACCESS_COARSE_LOCATION)
        }
    }*/