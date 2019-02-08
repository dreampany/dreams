package com.dreampany.frame.api.service

import android.app.Service
import androidx.annotation.CallSuper
import com.dreampany.frame.misc.AppExecutors
import dagger.android.*
import javax.inject.Inject

/**
 * Created by Roman-372 on 2/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

abstract class BaseService : DaggerService(), HasServiceInjector {

    @Inject
    internal lateinit var serviceInjector: DispatchingAndroidInjector<Service>
    @Inject
    internal lateinit var ex: AppExecutors

    @CallSuper
    override fun onCreate() {
        super.onCreate()
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return serviceInjector
    }

}