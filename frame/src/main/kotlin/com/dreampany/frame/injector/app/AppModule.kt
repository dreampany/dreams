package com.dreampany.frame.injector.app

import android.app.Application
import android.content.Context
import com.dreampany.frame.app.BaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


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