package com.dreampany.tools.injector.service

import com.dreampany.tools.service.AppService
import com.dreampany.tools.service.NotifyService
import com.dreampany.tools.service.player.PlayerService
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 7/23/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun notifyService(): NotifyService

    @ContributesAndroidInjector
    abstract fun appService(): AppService

    @ContributesAndroidInjector
    abstract fun playerService(): PlayerService
}