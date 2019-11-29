package com.dreampany.map

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.google.android.libraries.places.api.Places
import timber.log.Timber

/**
 * Created by roman on 2019-11-28
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Places.initialize(this, getString(R.string.google_maps_key));
    }
}