package com.dreampany.framework.data.source.pref

import android.content.Context
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.framework.misc.util.Util
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
        return Constant.Keys.PrefKeys.AD
    }

    fun setBannerTime(time: Long) {
        setPrivately(Constant.Keys.Ad.BANNER, time)
    }

    fun setInterstitialTime(time: Long) {
        setPrivately(Constant.Keys.Ad.INTERSTITIAL, time)
    }

    fun setRewardedTime(time: Long) {
        setPrivately(Constant.Keys.Ad.REWARDED, time)
    }

    fun isBannerExpired(expireTime: Long): Boolean {
        updateIfMissing(Constant.Keys.Ad.BANNER, Util.currentMillis())
        val time = getPrivately(Constant.Keys.Ad.BANNER, Constant.Default.LONG)
        return time.isExpired(expireTime)
    }

    fun isInterstitialExpired(expireTime: Long): Boolean {
        updateIfMissing(Constant.Keys.Ad.INTERSTITIAL, Util.currentMillis())
        val time = getPrivately(Constant.Keys.Ad.INTERSTITIAL, Constant.Default.LONG)
        return time.isExpired(expireTime)
    }

    fun isRewardedExpired(expireTime: Long): Boolean {
        updateIfMissing(Constant.Keys.Ad.REWARDED, Util.currentMillis())
        val time = getPrivately(Constant.Keys.Ad.REWARDED, Constant.Default.LONG)
        return time.isExpired(expireTime)
    }

    private fun updateIfMissing(key: String, value: Long) {
        if (!hasPrivate(key)) {
            setPrivately(key, value)
        }
    }
}