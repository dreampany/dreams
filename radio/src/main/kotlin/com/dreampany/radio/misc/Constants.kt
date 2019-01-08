package com.dreampany.radio.misc

import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
object Constants {

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
        val Banner = TimeUnit.MINUTES.toMillis(1)
        val Interstitial = TimeUnit.MINUTES.toMillis(10)
        val Rewarded = TimeUnit.MINUTES.toMillis(30)
    }
}