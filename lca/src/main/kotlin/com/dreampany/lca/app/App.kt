package com.dreampany.lca.app

import android.app.Activity
import com.crashlytics.android.Crashlytics
import com.dreampany.frame.app.BaseApp
import com.dreampany.frame.misc.SmartAd
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.lca.BuildConfig
import com.dreampany.lca.R
import com.dreampany.lca.data.enums.CoinSource
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.source.pref.Pref
import com.dreampany.lca.injector.app.DaggerAppComponent
import com.dreampany.lca.misc.Constants
import com.dreampany.lca.service.NotifyService
import com.dreampany.lca.worker.NotifyWorker
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

/*    @Inject
    lateinit var notify: NotifyViewModel*/

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

    override fun hasTheme(): Boolean {
        return false
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

    override fun getScreen(): String {
        return Constants.app(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        if (!isDebug() && hasCrashlytics()) {
            configFabric()
        }
        configAd()
        configJob()
        //configWork()
        clean()
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
        val fabric = Fabric.Builder(this).kits(Crashlytics()).debuggable(isDebug()).build()
        Fabric.with(fabric)
    }

    private fun configAd() {
        //ad.initPoints(Util.AD_POINTS)
        val config = SmartAd.Config.Builder().bannerExpireDelay(TimeUnit.MINUTES.toMillis(0))
            .interstitialExpireDelay(TimeUnit.MINUTES.toMillis(10))
            .rewardedExpireDelay(TimeUnit.MINUTES.toMillis(30)).enabled(!isDebug())
        ad.setConfig(config.build())
    }

    private fun configJob() {
        if (pref.hasNotification()) {
            job.create(
                NotifyService::class.java,
                Constants.Delay.Notify.toInt(),
                Constants.Period.Notify.toInt()
            )
        } else {
            job.cancel(NotifyService::class.java)
        }
    }


    /**
    java.lang.IllegalArgumentException: could not find worker: androidx.work.impl.workers.ConstraintTrackingWorker
    at com.dreampany.frame.worker.factory.WorkerInjectorFactory.createWorker(WorkerInjectorFactory.kt:26)
     */
    private fun configWork() {
        if (pref.hasNotification()) {
            worker.createPeriodic(NotifyWorker::class, Constants.Period.Notify, TimeUnit.SECONDS)
        } else {
            worker.cancel(NotifyWorker::class)
        }
    }


    private fun clean() {
        if (isVersionUpgraded()) {
            val exists = pref.versionCode
            val current = AndroidUtil.getVersionCode(this)

            when (current) {
                127,
                126,
                125 -> {
                    if (exists < 125) {
                        val currency = pref.getCurrency(Currency.USD)
                        for (coinIndex in 0..10)
                            pref.clearCoinIndexTime(CoinSource.CMC.name, currency.name, coinIndex);
                    }
                }
                120 -> {
                    if (exists < 120) {
                        val currency = pref.getCurrency(Currency.USD)
                        for (coinIndex in 0..10)
                            pref.clearCoinIndexTime(CoinSource.CMC.name, currency.name, coinIndex);
                    }
                }
                117 -> {
                    if (exists < 117) {
                        val currency = pref.getCurrency(Currency.USD)
                        for (coinIndex in 0..10)
                            pref.clearCoinIndexTime(CoinSource.CMC.name, currency.name, coinIndex);
                    }
                }
                63 -> {
                    if (exists < 63) {
                        //pref.clearCoinListingTime()
                    }
                }
                59 -> {
                    if (exists < 58) {
                        // pref.clearCoinListingTime()
                    }
                }
                58 -> {
                    if (exists < 58) {
                        //pref.clearCoinListingTime()
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