package com.dreampany.tools.misc.constants

import java.util.concurrent.TimeUnit

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiConstants {

    object Keys {
        object PrefKeys {
            const val WIFI = "wifi"

            object Wifi {

            }
        }

        object Room {
            const val TYPE_WIFI = "wifi"
        }

        const val RSN = "RSN"
    }

    object Limits {
        const val WIFIS = 10L
    }

    object Times {
        val PERIODIC_SCAN = TimeUnit.SECONDS.toMillis(10)
    }
}