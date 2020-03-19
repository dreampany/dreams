package com.dreampany.tools.app

import com.dreampany.common.app.BaseInjectorApp
import com.dreampany.tools.injector.app.DaggerAppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class App : BaseInjectorApp() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onOpen() {
       initFresco()
    }

    override fun onClose() {

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
}