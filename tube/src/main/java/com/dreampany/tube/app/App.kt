package com.dreampany.tube.app

import android.annotation.SuppressLint
import android.os.Bundle
import com.dreampany.tube.R
import com.dreampany.tube.inject.app.DaggerAppComponent
import com.dreampany.tube.manager.AdsManager
import com.dreampany.framework.app.InjectApp
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.Indexable
import com.google.firebase.appindexing.builders.Indexables
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import java.util.HashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class App : InjectApp() {

    @Inject
    internal lateinit var ads: AdsManager

    private var analytics: FirebaseAnalytics? = null
    private var action: Action? = null
    private var indexable: Indexable? = null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this).build()

    override fun onOpen() {
        initFirebase()
        initCrashlytics()
        initIndexing()
        initAd()
        initFresco()
        startAppIndex()
        //configWork()
    }

    override fun onClose() {
        stopAppIndex()
    }

    override fun logEvent(params: Map<String, Map<String, Any>?>?) {
        params?.let {
            val key = it.keys.first()
            val param = it.values.first()
            val bundle = Bundle()
            param?.entries?.forEach { bundle.putString(it.key, it.value.toString()) }
            analytics?.logEvent(key, bundle)
        }
    }

    private fun initFirebase() {
        if (isDebug) return
        FirebaseApp.initializeApp(this)
        analytics = Firebase.analytics
        logEvent(params)
    }

    private fun initCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isDebug.not())
    }

    private fun initIndexing() {
        if (isDebug) return
        val name = getString(R.string.app_name)
        val description = getString(R.string.app_description)
        val url = getString(R.string.app_url)
        action = action(description, url);
        indexable = Indexables.newSimple(name, url)
    }

    @SuppressLint("MissingPermission")
    private fun initAd() {
        //if (isDebug) return
        MobileAds.initialize(this)
        //ad.initPoints(Util.AD_POINTS)
        val config = AdsManager.Config.Builder()
            .bannerExpireDelay(TimeUnit.MINUTES.toMillis(0))
            .interstitialExpireDelay(TimeUnit.MINUTES.toMillis(5))
            .rewardedExpireDelay(TimeUnit.MINUTES.toMillis(10))
            .enabled(!isDebug)
        ads.setConfig(config.build())
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

    private fun action(description: String, uri: String): Action {
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

    private val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, Constant.Param.screen(this))

            params.put(Constant.Event.key(this), param)
            return params
        }
}