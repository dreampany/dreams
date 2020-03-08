package com.dreampany.tools.injector.service

import com.dreampany.tools.service.AppService
import com.dreampany.tools.service.LockService
import com.dreampany.tools.service.NotifyService
import com.dreampany.tools.service.PlayerService
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by roman on 2019-09-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun notifyService(): NotifyService

    @ContributesAndroidInjector
    abstract fun appService(): AppService

    @ContributesAndroidInjector
    abstract fun playerService(): PlayerService

    @ContributesAndroidInjector
    abstract fun lockService(): LockService
}