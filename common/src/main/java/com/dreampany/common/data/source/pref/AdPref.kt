package com.dreampany.common.data.source.pref

import android.content.Context
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.extension.isExpired
import com.dreampany.common.misc.util.Util
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 6/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AdPref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constants.Keys.PrefKeys.AD
    }

    fun setBannerTime(time: Long) {
        setPrivately(Constants.Keys.Ad.BANNER, time)
    }

    fun setInterstitialTime(time: Long) {
        setPrivately(Constants.Keys.Ad.INTERSTITIAL, time)
    }

    fun setRewardedTime(time: Long) {
        setPrivately(Constants.Keys.Ad.REWARDED, time)
    }

    fun isBannerExpired(expireTime: Long): Boolean {
        updateIfMissing(Constants.Keys.Ad.BANNER, Util.currentMillis())
        val time = getPrivately(Constants.Keys.Ad.BANNER, Constants.Default.LONG)
        return time.isExpired( expireTime)
    }

    fun isInterstitialExpired(expireTime: Long): Boolean {
        updateIfMissing(Constants.Keys.Ad.INTERSTITIAL, Util.currentMillis())
        val time = getPrivately(Constants.Keys.Ad.INTERSTITIAL, Constants.Default.LONG)
        return time.isExpired(expireTime)
    }

    fun isRewardedExpired(expireTime: Long): Boolean {
        updateIfMissing(Constants.Keys.Ad.REWARDED, Util.currentMillis())
        val time = getPrivately(Constants.Keys.Ad.REWARDED, Constants.Default.LONG)
        return time.isExpired(expireTime)
    }

    private fun updateIfMissing(key: String, value: Long) {
        if (!hasPrivate(key)) {
            setPrivately(key, value)
        }
    }
}