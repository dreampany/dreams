package com.dreampany.network.api

import android.net.wifi.WifiManager
import com.dreampany.network.data.model.Network

/**
 * Created by Roman-372 on 7/1/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Wifi : NetworkApi {

    private lateinit var wifi: WifiManager

    constructor() {

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