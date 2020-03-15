package com.dreampany.pair.injector.data

import com.dreampany.common.injector.annote.Auth
import com.dreampany.common.injector.annote.Remote
import com.dreampany.common.injector.annote.Room
import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.source.auth.AuthAuthDataSource
import com.dreampany.pair.data.source.api.AuthDataSource
import com.dreampany.pair.data.source.remote.AuthRemoteDataSource
import com.dreampany.pair.data.source.room.dao.UserDao
import com.dreampany.pair.data.source.room.registration.AuthRoomDataSource
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
    @Room
    fun provideRegistrationRoomDataSource(
        dao: UserDao
    ): AuthDataSource {
        return AuthRoomDataSource(dao)
    }

    @Singleton
    @Provides
    @Auth
    fun provideRegistrationAuthDataSource(
        mappers: Mappers
    ): AuthDataSource {
        return AuthAuthDataSource(mappers)
    }

    @Singleton
    @Provides
    @Remote
    fun provideRegistrationRemoteDataSource(
        mappers: Mappers
    ): AuthDataSource {
        return AuthRemoteDataSource(mappers)
    }
}