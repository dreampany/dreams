package com.dreampany.demo.inject.ui.home

import com.dreampany.demo.ui.home.fragment.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class HomeModule {
    @ContributesAndroidInjector
    abstract fun home(): HomeFragment
}