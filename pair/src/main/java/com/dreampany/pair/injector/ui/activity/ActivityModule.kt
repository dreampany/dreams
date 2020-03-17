package com.dreampany.pair.injector.ui.activity

import com.dreampany.common.injector.annote.ActivityScope
import com.dreampany.pair.ui.auth.activity.AuthActivity
import com.dreampany.pair.ui.auth.activity.LoginActivity
import com.dreampany.pair.ui.auth.activity.RegistrationActivity
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
    abstract fun splashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun tutorialActivity(): TutorialActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun authActivity(): AuthActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun registrationActivity(): RegistrationActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun loginActivity(): LoginActivity
}