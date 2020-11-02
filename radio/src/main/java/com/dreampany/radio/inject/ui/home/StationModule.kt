package com.dreampany.radio.inject.ui.home

import com.dreampany.radio.ui.fragment.StationsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class StationModule {
    @ContributesAndroidInjector
    abstract fun stations(): StationsFragment
}