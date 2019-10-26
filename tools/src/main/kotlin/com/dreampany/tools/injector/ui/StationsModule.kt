package com.dreampany.tools.injector.ui

import com.dreampany.tools.ui.fragment.radio.StationsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2019-10-23
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class StationsModule {
    @ContributesAndroidInjector
    abstract fun stationsFragment(): StationsFragment
}