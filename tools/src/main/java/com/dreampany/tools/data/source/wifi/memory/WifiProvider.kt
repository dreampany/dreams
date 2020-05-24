package com.dreampany.tools.data.source.wifi.memory

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.dreampany.adapter.value
import com.dreampany.framework.misc.util.Util
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 23/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiProvider
@Inject constructor(
    private val context: Context
) {

    private val manager: WeakReference<WifiManager>

    init {
        val instance: WifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        manager = WeakReference(instance)
    }

    val isEnabled: Boolean
        get() {
            try {
                return manager.get()?.isWifiEnabled ?: false
            } catch (error: Throwable) {
                return false
            }
        }

    fun enable(callback: () -> Unit): Boolean {
        try {
            return isEnabled || enableWifi(callback)
        } catch (error: Throwable) {
            return false
        }
    }

    fun disable(callback: () -> Unit): Boolean {
        try {
            return !isEnabled || disableWifi(callback)
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
                if (manager.get()?.startScan() ?: false) {
                    return manager.get()?.scanResults ?: Collections.emptyList()
                }
            } catch (error: Throwable) {
                Timber.e(error)
            }
            return Collections.emptyList()
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
    private fun enableWifi(callback: () -> Unit): Boolean {
        if (Util.isMinQ()) {
            //activity.get().open(Settings.Panel.ACTION_WIFI, 0)
            callback()
            return true
        }
        return manager.get()?.setWifiEnabled(true) ?: false
    }

    @SuppressLint("MissingPermission")
    private fun disableWifi(callback: () -> Unit): Boolean {
        if (Util.isMinQ()) {
            //activity.get().open(Settings.Panel.ACTION_WIFI, 0)
            callback()
            return true
        }
        return manager.get()?.setWifiEnabled(false) ?: false
    }

}