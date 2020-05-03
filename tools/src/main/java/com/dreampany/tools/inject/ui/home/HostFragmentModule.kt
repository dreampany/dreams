package com.dreampany.tools.inject.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.dreampany.framework.inject.annote.FragmentKey
import com.dreampany.framework.ui.fragment.factory.InjectFragmentFactory
import com.dreampany.tools.ui.home.fragment.HomeFragment
import com.dreampany.tools.ui.more.MoreFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class HostFragmentModule {

    @Binds
    abstract fun bindFactory(factory: InjectFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(HomeFragment::class)
    abstract fun home(fragment: HomeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(MoreFragment::class)
    abstract fun more(fragment: MoreFragment): Fragment

}