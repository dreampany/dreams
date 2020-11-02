package com.dreampany.radio.inject.ui.home

import com.dreampany.radio.ui.fragment.SearchFragment
import com.dreampany.radio.ui.home.fragment.HomeFragment
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
    @ContributesAndroidInjector(modules = [StationModule::class])
    abstract fun home(): HomeFragment

    @ContributesAndroidInjector
    abstract fun search(): SearchFragment
}