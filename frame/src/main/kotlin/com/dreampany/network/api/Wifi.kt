package com.dreampany.network.api

import android.content.Context
import android.net.wifi.WifiManager
import com.dreampany.network.data.model.Network
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/1/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Wifi @Inject constructor(val context: Context) : NetworkApi() {

    private val wifi: WifiManager
    init {
        wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    override fun isEnabled(): Boolean {
        return wifi.isWifiEnabled()
    }

    override fun isConnected(): Boolean {
        if (isEnabled()) {
            val info = wifi.getConnectionInfo();
            return info.getNetworkId() != -1;
        } else {
            return false;
        }
    }

    override fun getNetwork(): Network {
        val network = Network(Network.Type.WIFI)
        network.enabled = isEnabled()
        network.connected = isConnected()
        return network
    }
}