package com.dreampany.frame.api.receiver

import android.content.BroadcastReceiver
import com.dreampany.frame.misc.AppExecutors
import dagger.android.AndroidInjector
import dagger.android.DaggerBroadcastReceiver
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasBroadcastReceiverInjector
import javax.inject.Inject

/**
 * Created by Roman-372 on 2/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseReceiver : DaggerBroadcastReceiver(), HasBroadcastReceiverInjector {

    @Inject
    internal lateinit var receiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    @Inject
    protected lateinit var ex: AppExecutors

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> {
        return receiverInjector
    }

}
