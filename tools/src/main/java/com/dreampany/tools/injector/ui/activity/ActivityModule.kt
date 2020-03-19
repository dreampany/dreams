package com.dreampany.tools.injector.ui.activity

import com.dreampany.common.injector.annote.ActivityScope
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
}