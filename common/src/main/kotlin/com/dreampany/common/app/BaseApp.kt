package com.dreampany.common.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.dreampany.common.BuildConfig
import com.dreampany.common.misc.AppExecutors
import dagger.android.support.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by Roman-372 on 5/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseApp : DaggerApplication(), Application.ActivityLifecycleCallbacks {

    @Inject
    protected lateinit var ex: AppExecutors
    protected var refs: WeakReference<Activity>? = null

    @Volatile
    protected var visible: Boolean = false

    open fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    open fun onOpen() {}

    open fun onClose() {}

    open fun onActivityOpen(activity: Activity) {}

    open fun onActivityClose(activity: Activity) {}

    override fun onCreate() {
        super.onCreate()
        if (isDebug()) {
            Timber.plant(Timber.DebugTree())
        }
        configureIt()
        configRx()
        onOpen()
    }

    override fun onTerminate() {
        onClose()
        super.onTerminate()
    }

    override fun onActivityPaused(activity: Activity) {
        visible = false
    }

    override fun onActivityResumed(activity: Activity) {
        visible = true
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        onActivityClose(activity)
        refs?.clear()
    }

    override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        onActivityClose(activity)
        refs = WeakReference(activity)
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

    private fun configureIt() {
        registerActivityLifecycleCallbacks(this)
    }

    private fun configRx() {
        RxJavaPlugins.setErrorHandler {
            Timber.e(it, "Rx Global Error Handler.")

        }
    }
}