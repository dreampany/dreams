package com.dreampany.tools.injector.receiver

import com.dreampany.tools.receiver.BlockCallReceiver
import com.dreampany.tools.receiver.BootReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2019-09-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ReceiverModule {
    @ContributesAndroidInjector
    abstract fun bootReceiver(): BootReceiver

    @ContributesAndroidInjector
    abstract fun blockCallReceiver(): BlockCallReceiver
}