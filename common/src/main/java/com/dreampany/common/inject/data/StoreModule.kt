package com.dreampany.common.inject.data

import android.app.Application
import com.dreampany.common.data.source.api.StoreDataSource
import com.dreampany.common.data.source.mapper.StoreMapper
import com.dreampany.common.data.source.room.StoreRoomDataSource
import com.dreampany.common.data.source.room.dao.StoreDao
import com.dreampany.common.data.source.room.database.DatabaseManager
import com.dreampany.common.inject.annote.Room
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
class StoreModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideCoinDao(database: DatabaseManager): StoreDao = database.storeDao()

    @Singleton
    @Provides
    @Room
    fun provideStoreRoomDataSource(
        mapper: StoreMapper,
        dao: StoreDao
    ): StoreDataSource = StoreRoomDataSource(mapper, dao)
}