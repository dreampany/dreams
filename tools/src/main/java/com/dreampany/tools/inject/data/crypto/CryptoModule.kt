package com.dreampany.tools.inject.data.crypto

import android.app.Application
import android.content.Context
import com.dreampany.common.inject.annote.Remote
import com.dreampany.common.inject.annote.Room
import com.dreampany.common.misc.func.Keys
import com.dreampany.common.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.inject.data.CoinMarketCapModule
import com.dreampany.tools.api.crypto.inject.data.CryptoCompareModule
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapService
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
import com.dreampany.tools.data.source.crypto.remote.CoinRemoteDataSource
import com.dreampany.tools.data.source.crypto.room.CoinRoomDataSource
import com.dreampany.tools.data.source.crypto.room.dao.CoinDao
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao
import com.dreampany.tools.data.source.crypto.room.database.DatabaseManager
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
        CoinMarketCapModule::class,
        CryptoCompareModule::class
    ]
)
class CryptoModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager {
        return DatabaseManager.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideCoinDao(database: DatabaseManager): CoinDao {
        return database.coinDao()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(database: DatabaseManager): QuoteDao {
        return database.quoteDao()
    }

    @Singleton
    @Provides
    @Room
    fun provideRoomDataSource(
        mapper: CoinMapper,
        dao: CoinDao,
        quoteDao: QuoteDao
    ): CoinDataSource {
        return CoinRoomDataSource(mapper, dao, quoteDao)
    }
    @Singleton
    @Provides
    @Remote
    fun provideRemoteCoinDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: CoinMapper,
        service: CoinMarketCapService
    ): CoinDataSource {
        return CoinRemoteDataSource(
            context,
            network,
            parser,
            keys,
            mapper,
            service
        )
    }
/*

    @Singleton
    @Provides
    @Database
    fun provideDatabaseCoinDataSource(
        network: NetworkManager,
        mapper: CoinMapper,
        database: RxFirebaseDatabase
    ): CoinDataSource {
        return DatabaseCoinDataSource(network, mapper, database)
    }

    @Singleton
    @Provides
    @Remote
    fun provideRemoteTradeDataSource(
        context: Context,
        network: NetworkManager,
        keyM: KeyManager,
        mapper: TradeMapper,
        service: CryptoCompareService
    ): TradeDataSource {
        return TradeRemoteDataSource(
            context,
            network,
            keyM,
            mapper,
            service
        )
    }

    @Singleton
    @Provides
    @Remote
    fun provideExchangeRemoteDataSource(
        context: Context,
        network: NetworkManager,
        keyM: KeyManager,
        mapper: ExchangeMapper,
        service: CryptoCompareService
    ): ExchangeDataSource {
        return ExchangeRemoteDataSource(
            context,
            network,
            keyM,
            mapper,
            service
        )
    }*/
}