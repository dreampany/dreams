package com.dreampany.music.app

import com.dreampany.frame.app.BaseApp
import com.dreampany.music.BuildConfig
import com.dreampany.music.injector.app.DaggerAppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class App : BaseApp() {

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG;
    }

    override fun hasColor(): Boolean {
        return true
    }

    override fun applyColor(): Boolean {
        return true
    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(context)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        return DaggerAppComponent.builder().application(this).build()
    }
}