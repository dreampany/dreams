package com.dreampany.tools.inject.data

import android.app.Application
import com.dreampany.framework.inject.annote.Memory
import com.dreampany.framework.inject.annote.Room
import com.dreampany.tools.data.source.wifi.api.WifiDataSource
import com.dreampany.tools.data.source.wifi.mapper.WifiMapper
import com.dreampany.tools.data.source.wifi.memory.WifiMemoryDataSource
import com.dreampany.tools.data.source.wifi.memory.WifiProvider
import com.dreampany.tools.data.source.wifi.room.WifiRoomDataSource
import com.dreampany.tools.data.source.wifi.room.dao.WifiDao
import com.dreampany.tools.data.source.wifi.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class WifiModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideWifiDao(database: DatabaseManager): WifiDao = database.wifiDao()

    @Singleton
    @Provides
    @Memory
    fun provideWifiMemoryDataSource(
        mapper: WifiMapper,
        provider: WifiProvider
    ): WifiDataSource = WifiMemoryDataSource(mapper, provider)

    @Singleton
    @Provides
    @Room
    fun provideWifiRoomDataSource(
        mapper: WifiMapper,
        dao: WifiDao
    ): WifiDataSource = WifiRoomDataSource(mapper, dao)
}