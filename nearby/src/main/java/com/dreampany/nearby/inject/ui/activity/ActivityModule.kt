package com.dreampany.nearby.inject.ui.activity

import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.nearby.inject.ui.home.HomeModule
import com.dreampany.nearby.inject.ui.more.MoreModule
import com.dreampany.nearby.ui.home.activity.HomeActivity
import com.dreampany.nearby.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class, MoreModule::class])
    abstract fun home(): HomeActivity
}