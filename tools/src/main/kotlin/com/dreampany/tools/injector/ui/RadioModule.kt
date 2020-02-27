package com.dreampany.tools.injector.ui

import com.dreampany.framework.injector.annote.FragmentScope
import com.dreampany.tools.ui.fragment.radio.FavoriteStationsFragment
import com.dreampany.tools.ui.fragment.radio.RadioHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class RadioModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [StationsModule::class, SmallPlayerModule::class])
    abstract fun homeFragment(): RadioHomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun favoritesFragment(): FavoriteStationsFragment
}