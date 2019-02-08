package com.dreampany.fit.misc

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
}