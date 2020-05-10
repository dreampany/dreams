package com.dreampany.tools.inject.ui.history

import com.dreampany.tools.ui.history.fragment.HistoriesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class HistoriesModule {
    @ContributesAndroidInjector
    abstract fun histories(): HistoriesFragment
}