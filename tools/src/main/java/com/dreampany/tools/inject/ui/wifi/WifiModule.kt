package com.dreampany.tools.inject.ui.wifi

import com.dreampany.tools.ui.wifi.activity.WifisActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class WifiModule {
    @ContributesAndroidInjector
    abstract fun wifis(): WifisActivity
}