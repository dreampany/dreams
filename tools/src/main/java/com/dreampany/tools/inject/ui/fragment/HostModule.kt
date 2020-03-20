package com.dreampany.tools.inject.ui.fragment

import com.dreampany.common.ui.fragment.InjectHostFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class HostModule {
    @ContributesAndroidInjector(modules = [HostFragmentModule::class])
    abstract fun fragment(): InjectHostFragment
}