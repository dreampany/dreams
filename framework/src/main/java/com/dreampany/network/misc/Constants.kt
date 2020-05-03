package com.dreampany.network.misc

import java.util.*


/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
object Constants {

    object Default {
        val NULL = null
        const val BOOLEAN = false
        const val CHARACTER = 0.toChar()
        const val INT = 0
        const val LONG = 0L
        const val FLOAT = 0f
        const val DOUBLE = 0.0
        const val STRING = ""
        val LIST = Collections.emptyList<Any>()
    }

    object SECURITY {
        const val WEP = "WEP"
        const val PSK = "PSK"
        const val EAP = "EAP"
    }

    object Network {
        const val BSSID = "bssid"
        const val SSID = "ssid"
    }
}