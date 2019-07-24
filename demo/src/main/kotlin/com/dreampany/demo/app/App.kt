package com.dreampany.demo.app

import android.app.Activity
import com.crashlytics.android.Crashlytics
import com.dreampany.frame.app.BaseApp
import com.dreampany.demo.BuildConfig
import com.dreampany.demo.R
import com.dreampany.demo.data.source.pref.Pref
import com.dreampany.demo.injector.app.DaggerAppComponent
import com.dreampany.demo.misc.Constants
import com.dreampany.demo.service.NotifyService
import com.dreampany.frame.misc.SmartAd
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

    override fun hasAppIndex(): Boolean {
        return true
    }

    override fun hasUpdate(): Boolean {
        return true
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
            service.schedulePowerService(NotifyService::class.java, Constants.Time.NotifyPeriod.toInt())
        } else {
            service.cancel(NotifyService::class.java)
        }
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
}