package com.dreampany.hello.inject.ui.settings

import com.dreampany.hello.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 24/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class SettingsModule {
    @ContributesAndroidInjector
    abstract fun settings(): SettingsFragment
}