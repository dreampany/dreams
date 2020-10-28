package com.dreampany.news.inject.data

import android.app.Application
import android.content.Context
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.inject.data.DatabaseModule
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.news.api.news.inject.data.NewsApiModule
import com.dreampany.news.api.news.remote.service.NewsApiService
import com.dreampany.news.data.source.api.ArticleDataSource
import com.dreampany.news.data.source.api.PageDataSource
import com.dreampany.news.data.source.mapper.ArticleMapper
import com.dreampany.news.data.source.remote.ArticleRemoteDataSource
import com.dreampany.news.data.source.room.ArticleRoomDataSource
import com.dreampany.news.data.source.room.PageRoomDataSource
import com.dreampany.news.data.source.room.dao.ArticleDao
import com.dreampany.news.data.source.room.dao.PageDao
import com.dreampany.news.data.source.room.database.DatabaseManager
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
        NewsApiModule::class
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

    @Provides
    @Singleton
    fun provideArticleDao(database: DatabaseManager): ArticleDao = database.articleDao()

    @Singleton
    @Provides
    @Room
    fun providePageRoomDataSource(
        dao: PageDao
    ): PageDataSource = PageRoomDataSource(dao)

    @Singleton
    @Provides
    @Room
    fun provideNewsRoomDataSource(
        mapper: ArticleMapper,
        dao: ArticleDao
    ): ArticleDataSource = ArticleRoomDataSource(mapper, dao)

    @Singleton
    @Provides
    @Remote
    fun provideNewsRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: ArticleMapper,
        service: NewsApiService
    ): ArticleDataSource = ArticleRemoteDataSource(context, network, parser, keys, mapper, service)

}