package com.dreampany.wifi.scan

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Handler
import com.dreampany.wifi.data.source.WifiRepo
import java.lang.ref.WeakReference

/**
 * Created by roman on 22/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ScanServiceFactory private constructor() {
    companion object {
        fun instance(activity: Activity, handler: Handler): Scanner {
            val manager = activity.application.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val repo = WifiRepo(WeakReference(activity), WeakReference(manager))
            return Scanner(repo, handler)
        }
    }
}