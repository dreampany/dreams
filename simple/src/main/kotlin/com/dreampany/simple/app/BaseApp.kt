package com.dreampany.simple.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.dreampany.simple.BuildConfig
import dagger.android.support.DaggerApplication
import timber.log.Timber

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseApp : DaggerApplication(), Application.ActivityLifecycleCallbacks {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (isDebug()) {
            Timber.plant(Timber.DebugTree())
        }
    }

    open fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    open fun onOpen() {}

    open fun onClose() {}
}