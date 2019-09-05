package com.dreampany.framework.injector.data

import android.annotation.SuppressLint
import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/28/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
class FirebaseModule {
    @SuppressLint("MissingPermission")
    @Singleton
    @Provides
    fun provideFirebaseAnalytics(application: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(application.applicationContext)
    }
}