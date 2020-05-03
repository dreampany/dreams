package com.dreampany.frame.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.dreampany.frame.BuildConfig
import dagger.android.support.DaggerApplication
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseApp : DaggerApplication(), Application.ActivityLifecycleCallbacks {

    protected var refs: WeakReference<Activity>? = null
    @Volatile
    protected var visible: Boolean = false

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        if (isDebug()) {
            Timber.plant(Timber.DebugTree())
        }
        onOpen()
    }

    override fun onTerminate() {
        onClose()
        super.onTerminate()
    }

    open fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    open fun onOpen() {}

    open fun onClose() {}

    open fun onActivityOpen(activity: Activity) {}

    open fun onActivityClose(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        onActivityOpen(activity)
        refs = WeakReference(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        visible = true
    }

    override fun onActivityPaused(activity: Activity) {
        visible = false
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        onActivityClose(activity)
        refs?.clear()
    }

    fun isVisible(): Boolean {
        return visible
    }

    fun getCurrentUi(): Activity? {
        if (refs != null) {
            return refs?.get()
        } else {
            return null
        }
    }
}