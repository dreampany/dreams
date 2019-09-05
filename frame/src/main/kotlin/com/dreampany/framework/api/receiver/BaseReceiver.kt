package com.dreampany.framework.api.receiver

import com.dreampany.framework.misc.AppExecutors
import dagger.android.AndroidInjector
import dagger.android.DaggerBroadcastReceiver
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Created by Roman-372 on 2/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseReceiver : DaggerBroadcastReceiver(), HasAndroidInjector {

    @Inject
    internal lateinit var receiverInjector: DispatchingAndroidInjector<Any>

    @Inject
    protected lateinit var ex: AppExecutors

    override fun androidInjector(): AndroidInjector<Any> {
        return receiverInjector
    }
}
