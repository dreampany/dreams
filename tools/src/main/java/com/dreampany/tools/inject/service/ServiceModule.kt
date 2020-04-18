package com.dreampany.tools.inject.service

import com.dreampany.tools.service.RadioPlayerService
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun radioService(): RadioPlayerService

}