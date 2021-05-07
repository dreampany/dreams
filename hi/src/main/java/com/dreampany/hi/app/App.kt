package com.dreampany.hi.app

import androidx.multidex.MultiDexApplication
import com.dreampany.hi.BuildConfig
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : MultiDexApplication()  {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        Fresco.initialize(this)
    }
}