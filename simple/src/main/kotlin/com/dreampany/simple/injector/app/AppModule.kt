package com.dreampany.simple.injector.app

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module


/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application): Context
}