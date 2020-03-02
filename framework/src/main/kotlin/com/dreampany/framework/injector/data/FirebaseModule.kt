package com.dreampany.framework.injector.data

import android.annotation.SuppressLint
import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by roman on 2/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
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