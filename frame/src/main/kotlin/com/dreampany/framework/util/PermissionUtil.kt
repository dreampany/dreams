package com.dreampany.framework.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.util.*

/**
 * Created by roman on 2019-10-05
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PermissionUtil {
    companion object {
        fun hasPermissions(context: Context, vararg permissions: String): Boolean {
            if (permissions != null) {
                for (permission in permissions) {
                    if (!hasPermission(context, permission)) {
                        return false
                    }
                }
                return true
            }
            return false //when not decided or not expected solution
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        fun hasOverlayPermission(context: Context): Boolean {
            return Settings.canDrawOverlays(context.applicationContext)
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        fun hasWriteSettingsPermission(context: Context): Boolean {
            return Settings.System.canWrite(context.applicationContext)
        }

        fun checkOverlayPermission(activity: Activity, requestCode: Int): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return true
            }
            if (!hasOverlayPermission(activity)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.packageName)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivityForResult(intent, requestCode)
                return false
            } else {
                return true
            }
        }

        fun checkWriteSettingsPermission(activity: Activity, requestCode: Int): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return true
            }
            if (!hasWriteSettingsPermission(activity.applicationContext)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + activity.packageName)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivityForResult(intent, requestCode)
                return false
            } else {
                return true
            }
        }

        fun hasPermission(context: Context, permission: String): Boolean {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                hasPermissionInManifest(context, permission)
            } else ContextCompat.checkSelfPermission(
                context.applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun hasPermissionInManifest(context: Context, permission: String): Boolean {
            val packageInfo = AndroidUtil.getPackageInfo(context, PackageManager.GET_PERMISSIONS)
            if (packageInfo != null) {
                val requestedPermissions = packageInfo.requestedPermissions
                if (requestedPermissions != null) {
                    if (Arrays.asList(*requestedPermissions).contains(permission)) {
                        return true
                    }
                }
            }
            return false
        }
    }
}