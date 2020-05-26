package com.dreampany.scan.app

import com.dreampany.framework.app.InjectApp
import com.dreampany.framework.misc.extension.isDebug
import com.dreampany.scan.R
import com.dreampany.scan.inject.app.DaggerAppComponent
import com.dreampany.scan.manager.AdManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.Indexable
import com.google.firebase.appindexing.builders.Indexables
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
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
    internal lateinit var ad: AdManager

    private var action: Action? = null
    private var indexable: Indexable? = null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this).build()

    override fun onOpen() {
        initIndexing()
        initFirebase()
        initAd()
        initFresco()
        startAppIndex()
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
}