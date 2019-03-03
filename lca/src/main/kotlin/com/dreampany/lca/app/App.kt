package com.dreampany.lca.app

import android.app.Activity
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.dreampany.frame.app.BaseApp
import com.dreampany.frame.misc.SmartAd
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.lca.BuildConfig
import com.dreampany.lca.R
import com.dreampany.lca.data.source.pref.Pref
import com.dreampany.lca.injector.app.DaggerAppComponent
import com.dreampany.lca.misc.Constants
import com.dreampany.lca.service.NotifyService
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class App : BaseApp() {

    @Inject
    lateinit var pref: Pref

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG;
    }

    override fun hasCrashlytics(): Boolean {
        return true
    }

    override fun hasStetho(): Boolean {
        return false
    }

    override fun hasAppIndex(): Boolean {
        return true
    }

    override fun hasUpdate(): Boolean {
        return false
    }

    override fun hasRate(): Boolean {
        return true
    }

    override fun hasAd(): Boolean {
        return true
    }

    override fun hasColor(): Boolean {
        return true
    }

    override fun applyColor(): Boolean {
        return true
    }

    override fun getAdmobAppId(): Int {
        return R.string.admob_app_id
    }

    override fun onCreate() {
        super.onCreate()
        if (!isDebug() && hasCrashlytics()) {
            configFabric()
        }
        configAd()
        if (pref.hasNotification()) {
            job.create(NotifyService::class.java, Constants.Period.Notify.toInt(), Constants.Delay.Notify.toInt())
        } else {
            job.cancel(NotifyService::class.java)
        }
        clean()
        throwAppAnalytics()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onActivityOpen(activity: Activity) {
        super.onActivityOpen(activity)
    }

    override fun onActivityClose(activity: Activity) {
        super.onActivityClose(activity)
    }

    private fun configFabric() {
        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())
            .debuggable(isDebug())
            .build()
        Fabric.with(fabric)
    }

    private fun configAd() {
        //ad.initPoints(Util.AD_POINTS)
        val config = SmartAd.Config.Builder()
            .bannerExpireDelay(TimeUnit.MINUTES.toMillis(1))
            .interstitialExpireDelay(TimeUnit.MINUTES.toMillis(10))
            .rewardedExpireDelay(TimeUnit.MINUTES.toMillis(30))
            .enabled(!isDebug())
        ad.setConfig(config.build())
    }

    private fun throwAppAnalytics() {
        val current = AndroidUtil.getVersionCode(this)
        val bundle = Bundle()
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, current);
        getAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun clean() {
        if (isVersionUpgraded()) {
            val exists = pref.versionCode
            val current = AndroidUtil.getVersionCode(this)

            when(current) {
                62 -> {
                    if (exists < 62) {
                        pref.clearCoinListingTime()
                    }
                }
                59 -> {
                    if (exists < 58) {
                        pref.clearCoinListingTime()
                    }
                }
                58 -> {
                    if (exists < 58) {
                        pref.clearCoinListingTime()
                    }
                }
            }
            pref.versionCode = current
        }
    }

    private fun isVersionUpgraded(): Boolean {
        val exists = pref.versionCode
        val current = AndroidUtil.getVersionCode(this)
        if (current != exists) {
            return true
        }
        return false
    }
}