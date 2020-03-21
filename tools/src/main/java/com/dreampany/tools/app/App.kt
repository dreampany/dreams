package com.dreampany.tools.app

import com.dreampany.common.app.BaseInjectorApp
import com.dreampany.common.misc.extension.isDebug
import com.dreampany.tools.R
import com.dreampany.tools.inject.app.DaggerAppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import com.google.android.gms.ads.MobileAds
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.Indexable
import com.google.firebase.appindexing.builders.Indexables
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class App : BaseInjectorApp() {

    private var action: Action? = null
    private var indexable: Indexable? = null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onOpen() {
        initIndexing()
        initAd()
        initFresco()

        startAppIndex()
    }

    override fun onClose() {
        stopAppIndex()
    }

    private fun initIndexing() {
        val name = getString(R.string.app_name)
        val description = getString(R.string.app_description)
        val url = getString(R.string.app_url)
        action = getAction(description, url);
        indexable = Indexables.newSimple(name, url)
    }

    private fun initAd() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
    }

    private fun initFresco() {
        Fresco.initialize(
            this, ImagePipelineConfig.newBuilder(this)
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .experiment()
                .setNativeCodeDisabled(true)
                .build()
        )
    }

    private fun getAction(description: String, uri: String): Action {
        return Action.Builder(Action.Builder.VIEW_ACTION).setObject(description, uri).build()
    }

    private fun startAppIndex() {
        //f (isDebug()) return
        FirebaseAppIndex.getInstance().update(indexable)
        FirebaseUserActions.getInstance().start(action)
    }

    private fun stopAppIndex() {
        //if (isDebug()) return
        FirebaseUserActions.getInstance().end(action)
    }
}