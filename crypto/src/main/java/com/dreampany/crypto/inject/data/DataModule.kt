package com.dreampany.crypto.inject.data

import android.app.Application
import android.content.Context
import com.dreampany.crypto.api.inject.data.CoinMarketCapModule
import com.dreampany.crypto.api.inject.data.CryptoCompareModule
import com.dreampany.crypto.api.remote.service.CoinMarketCapService
import com.dreampany.crypto.api.remote.service.CryptoCompareService
import com.dreampany.crypto.data.source.api.CoinDataSource
import com.dreampany.crypto.data.source.api.ExchangeDataSource
import com.dreampany.crypto.data.source.api.TradeDataSource
import com.dreampany.crypto.data.source.mapper.CoinMapper
import com.dreampany.crypto.data.source.mapper.ExchangeMapper
import com.dreampany.crypto.data.source.mapper.TradeMapper
import com.dreampany.crypto.data.source.remote.CoinRemoteDataSource
import com.dreampany.crypto.data.source.remote.ExchangeRemoteDataSource
import com.dreampany.crypto.data.source.remote.TradeRemoteDataSource
import com.dreampany.crypto.data.source.room.CoinRoomDataSource
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
        CryptoCompareModule::class
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
    ): ExchangeDataSource = ExchangeRemoteDataSource(context, network, parser, keys, mapper, service)
}