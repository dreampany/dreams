package com.dreampany.frame.api.service

import android.app.Service
import com.dreampany.frame.misc.AppExecutors
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

/**
 * Created by Roman-372 on 2/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

abstract class BaseService : Service(), HasServiceInjector {

    @Inject
    internal lateinit var serviceInjector: DispatchingAndroidInjector<Service>
    @Inject
    internal lateinit var ex: AppExecutors

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return serviceInjector
    }

}