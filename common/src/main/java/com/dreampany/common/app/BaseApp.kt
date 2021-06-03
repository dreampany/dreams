package com.dreampany.common.app

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.dreampany.common.misc.exts.isDebug
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * Created by roman on 6/3/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class BaseApp : MultiDexApplication() {

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

    override fun onCreate() {
        super.onCreate()
        if (isDebug)
            Timber.plant(Timber.DebugTree())
        onOpen()
    }

    override fun onTerminate() {
        onClose()
        super.onTerminate()
    }
}