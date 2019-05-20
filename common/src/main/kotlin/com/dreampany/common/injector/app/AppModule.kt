package com.dreampany.common.injector.app

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

/**
 * Created by Roman-372 on 5/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application): Context
}