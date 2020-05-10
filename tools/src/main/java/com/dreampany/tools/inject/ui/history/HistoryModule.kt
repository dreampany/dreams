package com.dreampany.tools.inject.ui.history

import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.tools.ui.history.activity.HistoriesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class HistoryModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [HistoriesModule::class])
    abstract fun histories(): HistoriesActivity
}