package com.dreampany.wifi.data.source

import android.annotation.SuppressLint
import android.app.Activity
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.provider.Settings
import com.dreampany.wifi.misc.Util
import com.dreampany.wifi.misc.open
import java.lang.ref.WeakReference
import java.util.*

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

    val startScan: Boolean
        @SuppressLint("MissingPermission")
        get() {
            try {
                return manager.get()?.startScan() ?: false
            } catch (error: Throwable) {
                return false
            }
        }

    val scanResults: List<ScanResult>
        @SuppressLint("MissingPermission")
        get() {
            try {
                return manager.get()?.scanResults ?: Collections.emptyList()
            } catch (error: Throwable) {
                return Collections.emptyList()
            }
        }

    val wifiInfo: WifiInfo?
        @SuppressLint("MissingPermission")
        get() {
            try {
                return manager.get()?.connectionInfo
            } catch (error: Throwable) {
                return null
            }
        }

    @SuppressLint("MissingPermission")
    private fun enable(): Boolean {
        if (Util.isMinQ()) {
            activity.get().open(Settings.Panel.ACTION_WIFI, 0)
            return true
        }
        return manager.get()?.setWifiEnabled(true) ?: false
    }

    @SuppressLint("MissingPermission")
    private fun disable(): Boolean {
        if (Util.isMinQ()) {
            activity.get().open(Settings.Panel.ACTION_WIFI, 0)
            return true
        }
        return manager.get()?.setWifiEnabled(false) ?: false
    }

}