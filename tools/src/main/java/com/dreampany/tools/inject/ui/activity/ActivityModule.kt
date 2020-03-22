package com.dreampany.tools.inject.ui.activity

import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.tools.inject.ui.fragment.HomeModule
import com.dreampany.tools.ui.crypto.activity.CryptoActivity
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
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

/*    @ContributesAndroidInjector(modules = [HostModule::class])
    abstract fun home(): HomeActivity*/

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun home(): HomeActivity

    @ContributesAndroidInjector
    abstract fun crypto(): CryptoActivity


}