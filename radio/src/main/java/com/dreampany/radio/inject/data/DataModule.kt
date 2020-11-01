package com.dreampany.radio.inject.data

import android.app.Application
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.inject.data.DatabaseModule
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.radio.api.radiobrowser.RadioBrowserModule
import com.dreampany.radio.api.radiobrowser.StationService
import com.dreampany.radio.data.source.api.PageDataSource
import com.dreampany.radio.data.source.api.StationDataSource
import com.dreampany.radio.data.source.mapper.StationMapper
import com.dreampany.radio.data.source.remote.StationRemoteDataSource
import com.dreampany.radio.data.source.room.PageRoomDataSource
import com.dreampany.radio.data.source.room.StationRoomDataSource
import com.dreampany.radio.data.source.room.dao.PageDao
import com.dreampany.radio.data.source.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 5/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        DatabaseModule::class,
        MiscModule::class,
        RadioBrowserModule::class
    ]
)
class DataModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun providePageDao(database: DatabaseManager): PageDao = database.pageDao()

    @Singleton
    @Provides
    @Room
    fun providePageRoomDataSource(
        dao: PageDao
    ): PageDataSource = PageRoomDataSource(dao)

    @Singleton
    @Provides
    @Remote
    fun provideRemoteStationDataSource(
        network: NetworkManager,
        parser: Parser,
        mapper: StationMapper,
        service: StationService
    ): StationDataSource = StationRemoteDataSource(network, parser, mapper, service)
}