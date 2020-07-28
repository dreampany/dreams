package com.dreampany.framework.inject.data

import android.app.Application
import com.dreampany.framework.data.source.api.StoreDataSource
import com.dreampany.framework.data.source.api.TimeDataSource
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.room.StoreRoomDataSource
import com.dreampany.framework.data.source.room.TimeRoomDataSource
import com.dreampany.framework.data.source.room.dao.StoreDao
import com.dreampany.framework.data.source.room.dao.TimeDao
import com.dreampany.framework.data.source.room.database.DatabaseManager
import com.dreampany.framework.inject.annote.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideStoreDao(database: DatabaseManager): StoreDao = database.storeDao()

    @Provides
    @Singleton
    fun provideTimeDao(database: DatabaseManager): TimeDao = database.timeDao()

    @Singleton
    @Provides
    @Room
    fun provideStoreRoomDataSource(
        mapper: StoreMapper,
        dao: StoreDao
    ): StoreDataSource = StoreRoomDataSource(mapper, dao)

    @Singleton
    @Provides
    @Room
    fun provideTimeRoomDataSource(
        dao: TimeDao
    ): TimeDataSource = TimeRoomDataSource( dao)
}