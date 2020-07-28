package com.dreampany.tube.inject.data

import android.app.Application
import android.content.Context
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.inject.data.DatabaseModule
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tube.api.inject.data.YoutubeModule
import com.dreampany.tube.api.remote.service.YoutubeService
import com.dreampany.tube.data.source.api.CategoryDataSource
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.mapper.CategoryMapper
import com.dreampany.tube.data.source.mapper.VideoMapper
import com.dreampany.tube.data.source.remote.CategoryRemoteDataSource
import com.dreampany.tube.data.source.remote.VideoRemoteDataSource
import com.dreampany.tube.data.source.room.CategoryRoomDataSource
import com.dreampany.tube.data.source.room.VideoRoomDataSource
import com.dreampany.tube.data.source.room.dao.CategoryDao
import com.dreampany.tube.data.source.room.dao.VideoDao
import com.dreampany.tube.data.source.room.database.DatabaseManager
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
        YoutubeModule::class
    ]
)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideCategoryDao(database: DatabaseManager): CategoryDao = database.categoryDao()

    @Provides
    @Singleton
    fun provideVideoDao(database: DatabaseManager): VideoDao = database.videoDao()

    @Singleton
    @Provides
    @Room
    fun provideCategoryRoomDataSource(
        mapper: CategoryMapper,
        dao: CategoryDao
    ): CategoryDataSource = CategoryRoomDataSource(mapper, dao)

    @Singleton
    @Provides
    @Remote
    fun provideCategoryRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: CategoryMapper,
        service: YoutubeService
    ): CategoryDataSource =
        CategoryRemoteDataSource(context, network, parser, keys, mapper, service)

    @Singleton
    @Provides
    @Room
    fun provideVideoRoomDataSource(
        mapper: VideoMapper,
        dao: VideoDao
    ): VideoDataSource = VideoRoomDataSource(mapper, dao)

    @Singleton
    @Provides
    @Remote
    fun provideVideoRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: VideoMapper,
        service: YoutubeService
    ): VideoDataSource = VideoRemoteDataSource(context, network, parser, keys, mapper, service)
}