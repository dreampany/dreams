package com.dreampany.radio.inject.service

import com.dreampany.radio.service.RadioPlayerService
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 5/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun radioService(): RadioPlayerService
}