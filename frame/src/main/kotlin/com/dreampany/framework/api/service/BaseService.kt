package com.dreampany.framework.api.service

import android.content.Intent
import android.os.IBinder
import androidx.annotation.CallSuper
import com.dreampany.framework.api.worker.WorkerManager
import com.dreampany.framework.misc.AppExecutors
import dagger.android.AndroidInjector
import dagger.android.DaggerService
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Created by Roman-372 on 2/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

abstract class BaseService : DaggerService(), HasAndroidInjector {

    @Inject
    internal lateinit var serviceInjector: DispatchingAndroidInjector<Any>
    @Inject
    internal lateinit var ex: AppExecutors
    @Inject
    protected lateinit var worker: WorkerManager

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

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return serviceInjector
    }

}