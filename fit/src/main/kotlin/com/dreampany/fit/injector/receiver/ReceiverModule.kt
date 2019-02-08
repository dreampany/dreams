package com.dreampany.fit.injector.receiver

import com.dreampany.fit.receiver.BootReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Roman-372 on 2/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ReceiverModule {

    @ContributesAndroidInjector
    abstract fun bootReceiver(): BootReceiver
}