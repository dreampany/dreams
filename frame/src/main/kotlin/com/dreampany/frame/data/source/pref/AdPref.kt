package com.dreampany.frame.data.source.pref

import android.content.Context
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.util.TimeUtilKt
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AdPref @Inject constructor(context: Context) : BasePref(context) {

    override fun getPrivatePrefName(context: Context): String? {
        return Constants.Ad.KEY
    }

    fun setBannerTime(time: Long) {
        setPrivately(Constants.AdTime.BANNER, time)
    }

    fun setInterstitialTime(time: Long) {
        setPrivately(Constants.AdTime.INTERSTITIAL, time)
    }

    fun setRewardedTime(time: Long) {
        setPrivately(Constants.AdTime.REWARDED, time)
    }

    fun isBannerTimeExpired(expireTime: Long): Boolean {
        updateIfMissing(Constants.AdTime.BANNER, TimeUtil.currentTime())
        val time = getPrivately(Constants.AdTime.BANNER, 0L)
        return TimeUtil.isExpired(time, expireTime)
    }

    fun isInterstitialTimeExpired(expireTime: Long): Boolean {
        updateIfMissing(Constants.AdTime.INTERSTITIAL, TimeUtilKt.currentMillis())
        val time = getPrivately(Constants.AdTime.INTERSTITIAL, 0L)
        return TimeUtil.isExpired(time, expireTime)
    }

    fun isRewardedTimeExpired(expireTime: Long): Boolean {
        updateIfMissing(Constants.AdTime.REWARDED, TimeUtilKt.currentMillis())
        val time = getPrivately(Constants.AdTime.REWARDED, 0L)
        return TimeUtil.isExpired(time, expireTime)
    }

    private fun updateIfMissing(key: String, value: Long) {
        if (!hasPrivate(key)) {
            setPrivately(key, value)
        }
    }
}