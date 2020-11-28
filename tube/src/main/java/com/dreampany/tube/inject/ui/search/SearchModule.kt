package com.dreampany.tube.inject.ui.search

import com.dreampany.framework.inject.annote.FragmentScope
import com.dreampany.tube.ui.home.fragment.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 11/28/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class SearchModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [FilterModule::class])
    abstract fun search(): SearchFragment
}