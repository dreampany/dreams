package com.dreampany.hello.inject.data

import android.app.Application
import com.dreampany.framework.inject.annote.Firestore
import com.dreampany.hello.api.FirestoreManager
import com.dreampany.hello.data.source.api.AuthDataSource
import com.dreampany.hello.data.source.api.UserDataSource
import com.dreampany.hello.data.source.firestore.AuthFirestoreDataSource
import com.dreampany.hello.data.source.firestore.UserFirestoreDataSource
import com.dreampany.hello.data.source.mapper.AuthMapper
import com.dreampany.hello.data.source.mapper.UserMapper
import com.dreampany.hello.data.source.room.dao.UserDao
import com.dreampany.hello.data.source.room.database.DatabaseManager
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
    fun provideAuthFirestoreDataSource(
        mapper: AuthMapper,
        firestore: FirestoreManager
    ): AuthDataSource = AuthFirestoreDataSource(mapper, firestore)
}