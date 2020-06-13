package com.dreampany.crypto.inject.data

import android.app.Application
import android.content.Context
import com.dreampany.crypto.api.inject.data.CoinMarketCapModule
import com.dreampany.crypto.api.inject.data.CryptoCompareModule
import com.dreampany.crypto.api.inject.data.GeckoModule
import com.dreampany.crypto.api.inject.data.NewsApiModule
import com.dreampany.crypto.api.remote.service.CoinMarketCapService
import com.dreampany.crypto.api.remote.service.CryptoCompareService
import com.dreampany.crypto.api.remote.service.GeckoService
import com.dreampany.crypto.api.remote.service.NewsApiService
import com.dreampany.crypto.data.source.api.*
import com.dreampany.crypto.data.source.mapper.*
import com.dreampany.crypto.data.source.remote.*
import com.dreampany.crypto.data.source.room.CoinRoomDataSource
import com.dreampany.crypto.data.source.room.ArticleRoomDataSource
import com.dreampany.crypto.data.source.room.dao.ArticleDao
import com.dreampany.crypto.data.source.room.dao.CoinDao
import com.dreampany.crypto.data.source.room.dao.QuoteDao
import com.dreampany.crypto.data.source.room.database.DatabaseManager
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.inject.data.StoreModule
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
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
        StoreModule::class,
        CoinMarketCapModule::class,
        CryptoCompareModule::class,
        GeckoModule::class,
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
    fun provideCoinDao(database: DatabaseManager): CoinDao = database.coinDao()

    @Provides
    @Singleton
    fun provideQuoteDao(database: DatabaseManager): QuoteDao = database.quoteDao()

    @Provides
    @Singleton
    fun provideArticleDao(database: DatabaseManager): ArticleDao = database.articleDao()

    @Singleton
    @Provides
    @Room
    fun provideCoinRoomDataSource(
        mapper: CoinMapper,
        dao: CoinDao,
        quoteDao: QuoteDao
    ): CoinDataSource = CoinRoomDataSource(mapper, dao, quoteDao)

    @Singleton
    @Provides
    @Remote
    fun provideCoinRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: CoinMapper,
        service: CoinMarketCapService
    ): CoinDataSource = CoinRemoteDataSource(context, network, parser, keys, mapper, service)

    @Singleton
    @Provides
    @Remote
    fun provideTradeRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: TradeMapper,
        service: CryptoCompareService
    ): TradeDataSource = TradeRemoteDataSource(context, network, parser, keys, mapper, service)

    @Singleton
    @Provides
    @Remote
    fun provideExchangeRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: ExchangeMapper,
        service: CryptoCompareService
    ): ExchangeDataSource =
        ExchangeRemoteDataSource(context, network, parser, keys, mapper, service)

    @Singleton
    @Provides
    @Remote
    fun provideTickerRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: TickerMapper,
        service: GeckoService
    ): TickerDataSource = TickerRemoteDataSource(context, network, parser, keys, mapper, service)

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