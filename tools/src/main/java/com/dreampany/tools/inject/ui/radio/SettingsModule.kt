package com.dreampany.tools.inject.ui.radio

import com.dreampany.tools.ui.news.fragment.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 8/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class SettingsModule {
    @ContributesAndroidInjector
    abstract fun settings(): SettingsFragment
}