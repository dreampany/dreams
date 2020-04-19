package com.dreampany.tools.inject.ui.radio

import com.dreampany.tools.ui.radio.fragment.StationsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class StationsModule {
    @ContributesAndroidInjector
    abstract fun stations(): StationsFragment
}