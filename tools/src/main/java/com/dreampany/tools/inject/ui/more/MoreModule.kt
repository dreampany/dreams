package com.dreampany.tools.inject.ui.more

import com.dreampany.tools.ui.home.fragment.HomeFragment
import com.dreampany.tools.ui.more.MoreFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class MoreModule {
    @ContributesAndroidInjector
    abstract fun more(): MoreFragment
}