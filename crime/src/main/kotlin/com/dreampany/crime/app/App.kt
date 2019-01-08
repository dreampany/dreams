package com.dreampany.crime.app

import com.dreampany.frame.app.BaseApp
import com.dreampany.crime.BuildConfig
import com.dreampany.crime.injector.app.DaggerAppComponent
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
        Fresco.initialize(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}