package com.dreampany.share.app

import com.crashlytics.android.Crashlytics
import com.dreampany.frame.app.BaseApp
import com.dreampany.frame.misc.SmartAd
import com.dreampany.share.BuildConfig
import com.dreampany.share.R
import com.dreampany.share.data.source.local.Pref
import com.dreampany.share.injector.app.DaggerAppComponent
import com.dreampany.share.misc.Constants
import com.dreampany.share.service.NotifyService
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
        return false
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
        configureFabric()
        configureAd()
        if (pref.hasNotification()) {
            service.schedulePowerService(NotifyService::class.java, Constants.Time.NotifyPeriod.toInt())
        } else {
            service.cancel(NotifyService::class.java)
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    private fun configureFabric() {
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)
    }

    private fun configureAd() {
        //ad.initPoints(Util.AD_POINTS)
        val config = SmartAd.Config.Builder()
                .bannerExpireDelay(TimeUnit.MINUTES.toMillis(1))
                .interstitialExpireDelay(TimeUnit.MINUTES.toMillis(5))
                .rewardedExpireDelay(TimeUnit.MINUTES.toMillis(20))
                .enabled(false)
        ad.setConfig(config.build())
    }
}