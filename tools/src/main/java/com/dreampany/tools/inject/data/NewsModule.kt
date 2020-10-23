package com.dreampany.tools.inject.data

import android.app.Application
import android.content.Context
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.news.inject.data.NewsApiModule
import com.dreampany.tools.api.news.remote.service.NewsApiService
import com.dreampany.tools.data.source.new.remote.ArticleRemoteDataSource
import com.dreampany.tools.data.source.news.api.ArticleDataSource
import com.dreampany.tools.data.source.news.api.PageDataSource
import com.dreampany.tools.data.source.news.mapper.NewsMapper
import com.dreampany.tools.data.source.news.room.ArticleRoomDataSource
import com.dreampany.tools.data.source.news.room.PageRoomDataSource
import com.dreampany.tools.data.source.news.room.dao.ArticleDao
import com.dreampany.tools.data.source.news.room.dao.PageDao
import com.dreampany.tools.data.source.news.room.database.DatabaseManager
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
        NewsApiModule::class
    ]
)
class NewsModule {

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
        mapper: NewsMapper,
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
        mapper: NewsMapper,
        service: NewsApiService
    ): ArticleDataSource = ArticleRemoteDataSource(context, network, parser, keys, mapper, service)
}