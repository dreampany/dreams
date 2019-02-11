package com.dreampany.fit.misc

import android.Manifest
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
object Constants {

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
    }

    object Id {
        const val NotifyForeground = 101
        const val NotifyGeneral = 102
        const val NotifyForegroundChannelId = "channel_101"
    }

    object Permission {
        const val Location = Manifest.permission.ACCESS_FINE_LOCATION
        const val BodySensor = Manifest.permission.BODY_SENSORS
    }
}