package com.dreampany.common.misc.extension

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.content.Context
import android.os.Process

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Context?.hasUsagePermission( ): Boolean {
    val appOps =  this?.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager ?: return false
    val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
        Process.myUid(), getPackageName())
    return mode == MODE_ALLOWED
}