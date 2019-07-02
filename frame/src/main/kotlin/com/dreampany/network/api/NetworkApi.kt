package com.dreampany.network.api

import com.dreampany.network.data.model.Network

/**
 * Created by Roman-372 on 7/1/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

abstract class NetworkApi {

    abstract fun isEnabled(): Boolean

    abstract fun isConnected(): Boolean

    abstract fun getNetwork(): Network

/*    protected fun getNetworkType(connectivityType: Int): Network.Type {
        when (connectivityType) {
            ConnectivityManager.TYPE_MOBILE
        }
    }*/
}
