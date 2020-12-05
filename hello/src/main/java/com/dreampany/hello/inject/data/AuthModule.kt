package com.dreampany.hello.inject.data

import android.content.Context
import com.dreampany.framework.inject.annote.Firestore
import com.dreampany.hello.manager.FirestoreManager
import com.dreampany.hello.data.source.api.AuthDataSource
import com.dreampany.hello.data.source.firestore.AuthFirestoreDataSource
import com.dreampany.hello.data.source.mapper.AuthMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class AuthModule {

    @Singleton
    @Provides
    @Firestore
    fun provideFirestore(
        context: Context,
        mapper: AuthMapper,
        firestore: FirestoreManager
    ): AuthDataSource = AuthFirestoreDataSource(context, mapper, firestore)
}