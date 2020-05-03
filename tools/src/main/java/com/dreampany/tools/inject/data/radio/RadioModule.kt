package com.dreampany.tools.inject.data.radio

import android.app.Application
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.radio.RadioBrowserModule
import com.dreampany.tools.api.radio.StationService
import com.dreampany.tools.data.source.radio.api.StationDataSource
import com.dreampany.tools.data.source.radio.mapper.StationMapper
import com.dreampany.tools.data.source.radio.remote.StationRemoteDataSource
import com.dreampany.tools.data.source.radio.room.StationRoomDataSource
import com.dreampany.tools.data.source.radio.room.dao.StationDao
import com.dreampany.tools.data.source.radio.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        RadioBrowserModule::class
    ]
)
class RadioModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager = DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideStationDao(database: DatabaseManager): StationDao = database.stationDao()

    @Singleton
    @Provides
    @Room
    fun provideStationRoomDataSource(
        mapper: StationMapper,
        dao: StationDao
    ): StationDataSource = StationRoomDataSource(mapper, dao)

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