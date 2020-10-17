package com.dreampany.framework.data.source.pref

import android.content.Context
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.framework.misc.exts.isExpired
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 6/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AdsPref
@Inject constructor(
    context: Context
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constant.Keys.Pref.ADS

    fun setBannerTime(time: Long) {
        setPrivately(Constant.Keys.Ads.BANNER, time)
    }

    fun isBannerExpired(expireTime: Long): Boolean {
        updateIfMissing(Constant.Keys.Ads.BANNER, currentMillis)
        val time = getPrivately(Constant.Keys.Ads.BANNER, Constant.Default.LONG)
        return time.isExpired(expireTime)
    }

    fun setInterstitialTime(time: Long) {
        setPrivately(Constant.Keys.Ads.INTERSTITIAL, time)
    }

    fun isInterstitialExpired(expireTime: Long): Boolean {
        updateIfMissing(Constant.Keys.Ads.INTERSTITIAL, currentMillis)
        val time = getPrivately(Constant.Keys.Ads.INTERSTITIAL, Constant.Default.LONG)
        return time.isExpired(expireTime)
    }

    fun setRewardedTime(time: Long) {
        setPrivately(Constant.Keys.Ads.REWARDED, time)
    }

    fun isRewardedExpired(expireTime: Long): Boolean {
        updateIfMissing(Constant.Keys.Ads.REWARDED, currentMillis)
        val time = getPrivately(Constant.Keys.Ads.REWARDED, Constant.Default.LONG)
        return time.isExpired(expireTime)
    }

    fun setHouseTime(time: Long) {
        setPrivately(Constant.Keys.Ads.HOUSE, time)
    }

    fun isHouseExpired(expireTime: Long): Boolean {
        updateIfMissing(Constant.Keys.Ads.HOUSE, currentMillis)
        val time = getPrivately(Constant.Keys.Ads.HOUSE, Constant.Default.LONG)
        return time.isExpired(expireTime)
    }

    private fun updateIfMissing(key: String, value: Long) {
        if (!hasPrivate(key)) {
            setPrivately(key, value)
        }
    }
}