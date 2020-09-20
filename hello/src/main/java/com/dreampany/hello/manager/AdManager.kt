package com.dreampany.hello.manager

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import com.dreampany.ads.HouseAdsDialog
import com.dreampany.hello.R
import com.dreampany.framework.data.source.pref.AdPref
import com.dreampany.framework.misc.exts.gone
import com.dreampany.framework.misc.exts.visible
import com.dreampany.framework.misc.structure.MutablePair
import com.dreampany.framework.misc.util.Util
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.common.collect.Maps
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AdManager
@Inject constructor(
    private val context: Context,
    private val pref: AdPref
) {

    private enum class State {
        DEFAULT, FAILED, LOADED, OPENED, STARTED, RESUMED, PAUSED, CLICKED, LEFT, COMPLETED, CLOSED
    }

    private val defaultAdDelay = TimeUnit.SECONDS.toMillis(3)

    private val banners: MutableMap<String, MutablePair<AdView, State>> = Maps.newConcurrentMap()
    private val interstitials: MutableMap<String, MutablePair<InterstitialAd, State>> =
        Maps.newConcurrentMap()
    private val rewardeds: MutableMap<String, MutablePair<RewardedAd, State>> =
        Maps.newConcurrentMap()

    private val interstitialState: State = State.DEFAULT
    private val rewardedState: State = State.DEFAULT

    private val BANNER_MULTIPLIER = 1
    private val INTERSTITIAL_MULTIPLIER = 2
    private val REWARDED_MULTIPLIER = 4

    private var points: Long = 0
    private lateinit var config: Config

    private var ads: HouseAdsDialog? = null

    fun setConfig(config: Config) {
        this.config = config
    }

    fun initAd(
        context: Context,
        screenId: String,
        banner: AdView,
        @StringRes interstitialId: Int,
        @StringRes rewardedId: Int
    ) {
        if (!config.enabled) {
            return
        }
        initBanner(screenId, banner)
        initInterstitial(context, screenId, interstitialId)
        initRewarded(context, screenId, rewardedId)
    }


    fun initBanner(
        screenId: String,
        banner: AdView
    ) {
        banners.put(screenId, MutablePair(banner, State.DEFAULT))
        if (banner.adListener == null) {
            banner.setAdListener(object : BannerListener(screenId) {
                override fun onAdFailedToLoad(errorCode: Int) {
                    super.onAdFailedToLoad(errorCode)
                    banners[screenId]?.second = State.FAILED
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    banners[screenId]?.second = State.LOADED
                    val view = banners[screenId]?.first?.parent as View?
                    view?.visible()
                    pref.setBannerTime(Util.currentMillis())
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    banners[screenId]?.second = State.OPENED
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    banners[screenId]?.second = State.CLICKED
                }

                override fun onAdLeftApplication() {
                    super.onAdLeftApplication()
                    banners[screenId]?.second = State.LEFT
                }
            })
        }
    }

    fun loadAd(screenId: String) {
        val loaded: Boolean = loadInterstitial(screenId)
        if (!loaded) {
            loadBanner(screenId)
        }
    }


    @SuppressLint("MissingPermission")
    fun loadBanner(screenId: String): Boolean {
        if (!pref.isBannerExpired(config.bannerExpireDelay)) {
            return false
        }
        if (!banners.containsKey(screenId)) {
            return false
        }
        val banner: AdView? = banners[screenId]?.first
        banner?.loadAd(AdRequest.Builder().build())
        return true
    }

    @SuppressLint("MissingPermission")
    fun resumeBanner(screenId: String) {
        if (!pref.isBannerExpired(config.bannerExpireDelay)) {
            return
        }
        if (!banners.containsKey(screenId)) {
            return
        }
        val banner: AdView? = banners[screenId]?.first
        val state: State? = banners[screenId]?.second
        if (state != State.LOADED) {
            return
        }
        banner?.resume()
        val view = banner?.parent as View?
        view?.visible()
    }


    @SuppressLint("MissingPermission")
    fun pauseBanner(screenId: String) {
        if (!pref.isBannerExpired(config.bannerExpireDelay)) {
            return
        }
        if (!banners.containsKey(screenId)) {
            return
        }
        val state: State? = banners[screenId]?.second
        if (state != State.LOADED) {
            return
        }
        val banner: AdView? = banners[screenId]?.first
        val view = banner?.parent as View?
        view?.gone()
        banner?.pause()
    }

    fun destroyBanner(screenId: String) {
        if (!pref.isBannerExpired(config.bannerExpireDelay)) {
            return
        }
        if (!banners.containsKey(screenId)) {
            return
        }
        val banner: AdView? = banners[screenId]?.first
        val view = banner?.parent as View?
        view?.gone()
        banner?.destroy()
    }

    fun initInterstitial(
        context: Context,
        screenId: String,
        @StringRes adUnitId: Int
    ) {
        val interstitial = InterstitialAd(context)
        interstitial.adUnitId = context.getString(adUnitId)
        interstitials[screenId] = MutablePair(interstitial, State.DEFAULT)
        if (interstitial.adListener == null) {
            interstitial.setAdListener(object : InterstitialListener(screenId) {
                override fun onAdFailedToLoad(errorCode: Int) {
                    super.onAdFailedToLoad(errorCode)
                    interstitials[screenId]?.second = State.FAILED
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    interstitials[screenId]?.second = State.LOADED
                    interstitials[screenId]?.first?.show()
                    pref.setInterstitialTime(Util.currentMillis())
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    interstitials[screenId]?.second = State.OPENED
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    interstitials[screenId]?.second = State.CLICKED
                }

                override fun onAdLeftApplication() {
                    super.onAdLeftApplication()
                    interstitials[screenId]?.second = State.LEFT
                }
            })
        }
    }

    fun loadInterstitial(screenId: String): Boolean {
        if (!pref.isInterstitialExpired(config.interstitialExpireDelay)) {
            return false
        }
        if (!interstitials.containsKey(screenId)) {
            return false
        }
        val interstitial: InterstitialAd? = interstitials[screenId]?.first
        interstitial?.loadAd(AdRequest.Builder().build())
        return true
    }


    @SuppressLint("MissingPermission")
    fun resumeInterstitial(screenId: String) {
        if (!pref.isInterstitialExpired(config.interstitialExpireDelay)) {
            return
        }
        if (!interstitials.containsKey(screenId)) {
            return
        }
        val interstitial: InterstitialAd? = interstitials[screenId]?.first
    }


    @SuppressLint("MissingPermission")
    fun pauseInterstitial(screenId: String) {
        if (!pref.isInterstitialExpired(config.interstitialExpireDelay)) {
            return
        }
        if (!interstitials.containsKey(screenId)) {
            return
        }
        val interstitial: InterstitialAd? = interstitials[screenId]?.first
    }

    @SuppressLint("MissingPermission")
    fun destroyInterstitial(screenId: String) {
        if (!pref.isInterstitialExpired(config.interstitialExpireDelay)) {
            return
        }
        if (!interstitials.containsKey(screenId)) {
            return
        }
        val interstitial: InterstitialAd? = interstitials[screenId]?.first
        //interstitial.d();
    }


    fun initRewarded(
        context: Context,
        screenId: String,
        @StringRes adUnitId: Int
    ) {
        val rewarded = RewardedAd(context, context.getString(adUnitId))
        rewardeds[screenId] = MutablePair(rewarded, State.DEFAULT)
    }


    @SuppressLint("MissingPermission")
    fun loadRewarded(screenId: String) {
        if (!pref.isRewardedExpired(config.rewardedExpireDelay)) {
            //return;
        }
        if (!rewardeds.containsKey(screenId)) {
            return
        }
        val rewarded: RewardedAd? = rewardeds[screenId]?.first
        rewarded?.loadAd(
            AdRequest.Builder().build(),
            object : RewardedListener(screenId) {
                override fun onRewardedAdFailedToLoad(errorCode: Int) {
                    super.onRewardedAdFailedToLoad(errorCode)
                    rewardeds[screenId]?.second = State.FAILED
                }

                override fun onRewardedAdLoaded() {
                    super.onRewardedAdLoaded()
                    rewardeds[screenId]?.second = State.LOADED
                }
            })
    }


    @SuppressLint("MissingPermission")
    fun resumeRewarded(screenId: String) {
        if (!pref.isRewardedExpired(config.rewardedExpireDelay)) {
            return
        }
        if (!rewardeds.containsKey(screenId)) {
            return
        }
        val rewarded: RewardedAd? = rewardeds[screenId]?.first
    }


    @SuppressLint("MissingPermission")
    fun pauseRewarded(screenId: String) {
        if (!pref.isRewardedExpired(config.rewardedExpireDelay)) {
            return
        }
        if (!rewardeds.containsKey(screenId)) {
            return
        }
        val rewarded: RewardedAd? = rewardeds[screenId]?.first
    }


    @SuppressLint("MissingPermission")
    fun destroyRewarded(screenId: String) {
        if (!pref.isRewardedExpired(config.rewardedExpireDelay)) {
            return
        }
        if (!rewardeds.containsKey(screenId)) {
            return
        }
        val rewarded: RewardedAd? = rewardeds[screenId]?.first
    }

    fun showInHouseAds(context: Context) {
        ads = HouseAdsDialog(context, R.raw.apps).apply {
            hideIfAppInstalled(true)
            setCardCorners(16)
            setCtaCorner(16)
        }
        ads?.loadAds()
        ads?.setAdListener(object : com.dreampany.ads.AdListener {
            override fun onAdLoadFailed(exception: Exception) {

            }

            override fun onAdLoaded() {
                ads?.showAd()
            }

            override fun onAdClosed() {
            }

            override fun onAdShown() {
            }

            override fun onApplicationLeft() {
            }

        })
    }

    fun hideInHouseAds() {
        //  ads?.
    }

    class Config
    private constructor(
        val bannerExpireDelay: Long,
        val interstitialExpireDelay: Long,
        val rewardedExpireDelay: Long,
        val enabled: Boolean
    ) {

        class Builder {

            private var bannerExpireDelay: Long = 0
            private var interstitialExpireDelay: Long = 0
            private var rewardedExpireDelay: Long = 0
            private var enabled = false

            fun bannerExpireDelay(bannerExpireDelay: Long): Builder {
                this.bannerExpireDelay = bannerExpireDelay
                return this
            }

            fun interstitialExpireDelay(interstitialExpireDelay: Long): Builder {
                this.interstitialExpireDelay = interstitialExpireDelay
                return this
            }

            fun rewardedExpireDelay(rewardedExpireDelay: Long): Builder {
                this.rewardedExpireDelay = rewardedExpireDelay
                return this
            }

            fun enabled(enabled: Boolean): Builder {
                this.enabled = enabled
                return this
            }

            fun build(): Config {
                return Config(
                    bannerExpireDelay,
                    interstitialExpireDelay,
                    rewardedExpireDelay,
                    enabled
                )
            }
        }
    }

    /* listeners */
    private open class BannerListener internal constructor(val screenId: String) : AdListener()

    private open class InterstitialListener internal constructor(val screenId: String) :
        AdListener()

    private open class RewardedListener internal constructor(val screenId: String) :
        RewardedAdLoadCallback()
}