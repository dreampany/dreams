package com.dreampany.pair.injector.data

import android.content.Context
import com.dreampany.common.injector.annote.*
import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.source.auth.AuthFireauthDataSource
import com.dreampany.pair.data.source.api.AuthDataSource
import com.dreampany.pair.data.source.pref.AuthPrefDataSource
import com.dreampany.pair.data.source.repo.PrefRepo
import com.dreampany.pair.data.source.remote.AuthRemoteDataSource
import com.dreampany.pair.data.source.room.dao.UserDao
import com.dreampany.pair.data.source.room.registration.AuthRoomDataSource
import com.dreampany.pair.data.source.store.AuthFirestoreDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        RoomModule::class
    ]
)
class DataModule {

    @Singleton
    @Provides
    @Pref
    fun provideAuthPrefDataSource(
        pref: PrefRepo
    ): AuthDataSource {
        return AuthPrefDataSource(pref)
    }

    @Singleton
    @Provides
    @Room
    fun provideAuthRoomDataSource(
        dao: UserDao
    ): AuthDataSource {
        return AuthRoomDataSource(dao)
    }

    @Singleton
    @Provides
    @Remote
    fun provideAuthRemoteDataSource(
        mappers: Mappers
    ): AuthDataSource {
        return AuthRemoteDataSource(mappers)
    }

    @Singleton
    @Provides
    @Fireauth
    fun provideAuthFireauthDataSource(
        mappers: Mappers
    ): AuthDataSource {
        return AuthFireauthDataSource(mappers)
    }

    @Singleton
    @Provides
    @Firestore
    fun provideAuthFirestoreDataSource(
        context: Context,
        mappers: Mappers
    ): AuthDataSource {
        return AuthFirestoreDataSource(context, mappers)
    }

}