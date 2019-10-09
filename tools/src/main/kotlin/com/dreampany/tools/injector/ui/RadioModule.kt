package com.dreampany.tools.injector.ui

import com.dreampany.framework.misc.FragmentScope
import com.dreampany.tools.ui.fragment.RadioHomeFragment
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
    @ContributesAndroidInjector
    abstract fun homeFragment(): RadioHomeFragment
}