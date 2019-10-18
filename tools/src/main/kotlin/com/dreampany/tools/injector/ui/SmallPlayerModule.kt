package com.dreampany.tools.injector.ui

import com.dreampany.tools.ui.fragment.SmallPlayerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class SmallPlayerModule {
    @ContributesAndroidInjector
    abstract fun smallPlayerFragment(): SmallPlayerFragment
}