package com.dreampany.common.service

import android.content.Intent
import android.os.IBinder
import androidx.annotation.CallSuper
import com.dreampany.common.misc.func.Executors
import dagger.android.AndroidInjector
import dagger.android.DaggerService
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 14/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class InjectService: DaggerService(), HasAndroidInjector {

    @Inject
    internal lateinit var injector: DispatchingAndroidInjector<Any>
    @Inject
    protected lateinit var ex: Executors
    /*@Inject
    protected lateinit var worker: WorkerManager*/

    override fun androidInjector(): AndroidInjector<Any> = injector

    protected abstract fun onStart()

    protected abstract fun onStop()

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        onStart()
    }

    override fun onDestroy() {
        onStop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        Timber.v("${intent.component} wants to bind with the service")
        return null
    }
}