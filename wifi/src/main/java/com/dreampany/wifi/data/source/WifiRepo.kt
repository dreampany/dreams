package com.dreampany.wifi.data.source

import android.app.Activity
import android.net.wifi.WifiManager
import android.provider.Settings
import com.dreampany.wifi.misc.Util
import com.dreampany.wifi.misc.open
import java.lang.ref.WeakReference

/**
 * Created by roman on 21/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiRepo(
    private val activity: WeakReference<Activity>,
    private var manager: WeakReference<WifiManager>
) {

    val isEnabled: Boolean
        get() {
            try {
                return manager.get()?.isWifiEnabled ?: false
            } catch (error: Throwable) {
                return false
            }
        }

    val enable: Boolean
        get() {
            try {
                return isEnabled || enable()
            } catch (error: Throwable) {
                return false
            }
        }

    val disable: Boolean
        get() {
            try {
                return !isEnabled || disable()
            } catch (error: Throwable) {
                return false
            }
        }

    private fun enable(): Boolean {
        if (Util.isMinQ()) {
            activity.get().open(Settings.Panel.ACTION_WIFI, 0)
            return true
        }
        return manager.get()?.setWifiEnabled(true) ?: false
    }

    private fun disable(): Boolean {
        if (Util.isMinQ()) {
            activity.get().open(Settings.Panel.ACTION_WIFI, 0)
            return true
        }
        return manager.get()?.setWifiEnabled(false) ?: false
    }

}