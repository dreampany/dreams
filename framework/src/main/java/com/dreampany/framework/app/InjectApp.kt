package com.dreampany.framework.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.dreampany.framework.misc.extension.isDebug
import dagger.android.DaggerApplication
import timber.log.Timber
import java.lang.ref.WeakReference


/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class InjectApp : DaggerApplication(), Application.ActivityLifecycleCallbacks {

    @Volatile
    private var refs: WeakReference<Activity>? = null

    @Volatile
    private var visible: Boolean = false
        get() = field
        private set(value) {
            field = value
        }

    abstract fun onOpen()

    abstract fun onClose()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        if (isDebug)
            Timber.plant(Timber.DebugTree())
        onOpen()
    }

    override fun onTerminate() {
        onClose()
        super.onTerminate()
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        //onActivityOpen(activity)
        refs = WeakReference(activity)
        //goToRemoteUi()
        //if (hasUpdate()) {
            //startUpdate()
       // }
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
        //onActivityClose(activity)
        refs?.clear()
        //if (hasUpdate()) {
            //stopUpdate()
        //}
    }
}