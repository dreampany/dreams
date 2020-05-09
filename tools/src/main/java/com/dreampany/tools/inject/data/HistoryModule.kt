package com.dreampany.tools.inject.data

import android.app.Application
import android.content.Context
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.history.MuffinService
import com.dreampany.tools.api.history.inject.MuffinModule
import com.dreampany.tools.data.source.history.api.HistoryDataSource
import com.dreampany.tools.data.source.history.mapper.HistoryMapper
import com.dreampany.tools.data.source.history.remote.HistoryRemoteDataSource
import com.dreampany.tools.data.source.history.room.HistoryRoomDataSource
import com.dreampany.tools.data.source.history.room.dao.HistoryDao
import com.dreampany.tools.data.source.history.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        MuffinModule::class
    ]
)
class HistoryModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideHistoryDao(database: DatabaseManager): HistoryDao = database.historyDao()

    @Singleton
    @Provides
    @Room
    fun provideHistoryRoomDataSource(
        mapper: HistoryMapper,
        dao: HistoryDao
    ): HistoryDataSource = HistoryRoomDataSource(mapper, dao)

    @Singleton
    @Provides
    @Remote
    fun provideHistoryRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: HistoryMapper,
        service: MuffinService
    ): HistoryDataSource = HistoryRemoteDataSource(context, network, parser, keys, mapper, service)
}