package com.dreampany.hello.inject.data

import com.dreampany.framework.inject.annote.Firestore
import com.dreampany.hello.data.source.api.UserDataSource
import com.dreampany.hello.data.source.firestore.FirestoreManager
import com.dreampany.hello.data.source.firestore.UserFirestoreDataSource
import com.dreampany.hello.data.source.mapper.UserMapper
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
class UserModule {

    @Singleton
    @Provides
    @Firestore
    fun provideUserFirestoreDataSource(
        mapper: UserMapper,
        firestore: FirestoreManager
    ): UserDataSource = UserFirestoreDataSource(mapper, firestore)
}