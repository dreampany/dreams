package com.dreampany.lca.app
import android.annotation.SuppressLint
import com.dreampany.framework.app.InjectApp
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.lca.R
import com.dreampany.lca.data.source.pref.Pref
 import com.dreampany.lca.manager.AdManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.Indexable
import com.google.firebase.appindexing.builders.Indexables
import dagger.android.AndroidInjector
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class App : InjectApp() {

    @Inject
    internal lateinit var pref: Pref

    @Inject
    internal lateinit var ad: AdManager

    private var action: Action? = null
    private var indexable: Indexable? = null

    override fun applicationInjector(): AndroidInjector<out dagger.android.DaggerApplication> =
        DaggerAppComponent.builder().application(this).build()

    override fun onOpen() {
        initIndexing()
        initFirebase()
        initAd()
        initFresco()
        startAppIndex()
        configWork()
    }

    override fun onClose() {
        stopAppIndex()
    }

    private fun initIndexing() {
        if (isDebug) return
        val name = getString(R.string.app_name)
        val description = getString(R.string.app_description)
        val url = getString(R.string.app_url)
        action = getAction(description, url);
        indexable = Indexables.newSimple(name, url)
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
    }

    @SuppressLint("MissingPermission")
    private fun initAd() {
        //if (isDebug) return
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        //ad.initPoints(Util.AD_POINTS)
        val config = AdManager.Config.Builder()
            .bannerExpireDelay(TimeUnit.MINUTES.toMillis(0))
            .interstitialExpireDelay(TimeUnit.MINUTES.toMillis(5))
            .rewardedExpireDelay(TimeUnit.MINUTES.toMillis(10))
            .enabled(!isDebug)
        ad.setConfig(config.build())
    }

    private fun initFresco() {
        Fresco.initialize(
            this/*, ImagePipelineConfig.newBuilder(this)
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .experiment()
                .setNativeCodeDisabled(true)
                .build()*/
        )
    }

    private fun getAction(description: String, uri: String): Action {
        return Action.Builder(Action.Builder.VIEW_ACTION).setObject(description, uri).build()
    }

    private fun startAppIndex() {
        if (isDebug) return
        FirebaseAppIndex.getInstance().update(indexable)
        FirebaseUserActions.getInstance().start(action)
    }

    private fun stopAppIndex() {
        if (isDebug) return
        FirebaseUserActions.getInstance().end(action)
    }

    private fun configWork() {
        //worker.createPeriodic(CryptoWorker::class, CryptoConstants.Times.Crypto.WORKER, TimeUnit.HOURS)
    }


    /*override fun onCreate() {
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
                Constants.Tag.NOTIFY_SERVICE,
                NotifyService::class,
                Constants.Delay.Notify.toInt(),
                Constants.Period.Notify.toInt()
            )
        } else {
            job.cancel(Constants.Tag.NOTIFY_SERVICE)
        }
    }


    *//**
    java.lang.IllegalArgumentException: could not find worker: androidx.work.impl.workers.ConstraintTrackingWorker
    at com.dreampany.frame.worker.factory.WorkerInjectorFactory.createWorker(WorkerInjectorFactory.kt:26)
     *//*
    private fun configWork() {
        if (pref.hasNotification()) {
            worker.createPeriodic(NotifyWorker::class, Constants.Period.Notify, TimeUnit.SECONDS)
        } else {
            worker.cancel(NotifyWorker::class)
        }
    }

    private fun clean() {
        if (isVersionUpgraded()) {
            val exists = pref.getVersionCode()
            val current = AndroidUtil.getVersionCode(this)

            when (current) {
                146,
                145,
                144,
                143,
                142,
                141,
                140,
                139,
                138,
                137,
                136,
                135,
                134,
                133,
                132,
                131 -> {
                    if (exists < 131 || exists == 145) {
                        val currency = pref.getCurrency(Currency.USD)
                        for (coinIndex in 0..100)
                            pref.clearCoinIndexTime(CoinSource.CMC.name, currency.name, coinIndex);
                    }
                }
                130,
                129,
                128,
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
            pref.setVersionCode(current)
        }
    }

    private fun isVersionUpgraded(): Boolean {
        val exists = pref.getVersionCode()
        val current = AndroidUtil.getVersionCode(this)
        if (current != exists) {
            return true
        }
        return false
    }*/
}