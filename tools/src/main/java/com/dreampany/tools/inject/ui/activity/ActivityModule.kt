package com.dreampany.tools.inject.ui.activity

import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.tools.inject.ui.crypto.CryptoModule
import com.dreampany.tools.inject.ui.home.HomeModule
import com.dreampany.tools.inject.ui.more.MoreModule
import com.dreampany.tools.inject.ui.radio.RadioModule
import com.dreampany.tools.ui.home.activity.HomeActivity
import com.dreampany.tools.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        CryptoModule::class,
        RadioModule::class
    ]
)
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class, MoreModule::class])
    abstract fun home(): HomeActivity
}