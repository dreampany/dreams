package com.dreampany.tools.inject.ui.activity

import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.tools.inject.ui.fragment.HostModule
import com.dreampany.tools.ui.home.HomeActivity
import com.dreampany.tools.ui.home.HomeFragment
import com.dreampany.tools.ui.splash.SplashActivity
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
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HostModule::class])
    abstract fun home(): HomeActivity
}