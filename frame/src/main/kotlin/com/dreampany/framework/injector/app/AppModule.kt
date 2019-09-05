package com.dreampany.framework.injector.app

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module


/**
 * Created by Hawladar Roman on 5/23/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application): Context
}