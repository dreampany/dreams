package com.dreampany.pair.injector.ui.activity

import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.pair.ui.auth.activity.AuthActivity
import com.dreampany.pair.ui.auth.activity.LoginActivity
import com.dreampany.pair.ui.auth.activity.RegisterActivity
import com.dreampany.pair.ui.home.HomeActivity
import com.dreampany.pair.ui.splash.SplashActivity
import com.dreampany.pair.ui.tutorial.TutorialActivity
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
    @ContributesAndroidInjector
    abstract fun tutorial(): TutorialActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun auth(): AuthActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun register(): RegisterActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun login(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun home(): HomeActivity
}