package com.dreampany.radio.inject.ui.more

import com.dreampany.radio.ui.more.fragment.MoreFragment
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