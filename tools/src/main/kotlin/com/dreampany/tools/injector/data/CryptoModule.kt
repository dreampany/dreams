package com.dreampany.tools.injector.data

import androidx.core.util.Pair
import com.dreampany.firebase.RxFirebaseDatabase
import com.dreampany.framework.api.key.KeyManager
import com.dreampany.framework.injector.annote.Database
import com.dreampany.framework.injector.annote.Remote
import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.misc.*
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.injector.data.CoinMarketCapModule
import com.dreampany.tools.api.crypto.injector.data.CryptoCompareModule
import com.dreampany.tools.api.crypto.remote.service.CoinMarketCapService
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.mapper.crypto.CoinMapper
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.api.crypto.CoinDataSource
import com.dreampany.tools.data.source.database.DatabaseCoinDataSource
import com.dreampany.tools.data.source.remote.RemoteCoinDataSource
import com.dreampany.tools.data.source.room.RoomCoinDataSource
import com.dreampany.tools.data.source.room.dao.CoinDao
import com.dreampany.tools.data.source.room.dao.QuoteDao
import com.dreampany.tools.injector.annote.CurrencyAnnote
import com.dreampany.tools.injector.annote.QuoteAnnote
import com.dreampany.tools.injector.annote.crypto.*
import com.dreampany.tools.ui.model.CoinItem
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
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

    /* memory */
    @Singleton
    @Provides
    @CoinAnnote
    fun provideCoinSmartMap(): SmartMap<String, Coin> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @CoinAnnote
    fun provideCoinSmartCache(): SmartCache<String, Coin> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @CoinItemAnnote
    fun provideCoinItemSmartMap(): SmartMap<String, CoinItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @CoinItemAnnote
    fun provideCoinItemSmartCache(): SmartCache<String, CoinItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @CurrencyAnnote
    fun provideCurrencySmartMap(): SmartMap<String, Currency> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @CurrencyAnnote
    fun provideCurrencySmartCache(): SmartCache<String, Currency> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @QuoteAnnote
    fun provideQuoteSmartMap(): SmartMap<Pair<String, Currency>, Quote> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @QuoteAnnote
    fun provideQuoteSmartCache(): SmartCache<Pair<String, Currency>, Quote> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @TradeAnnote
    fun provideTradeSmartMap(): SmartMap<String, Coin> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @TradeAnnote
    fun provideTradeSmartCache(): SmartCache<String, Coin> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @TradeItemAnnote
    fun provideTradeItemSmartMap(): SmartMap<String, CoinItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @TradeItemAnnote
    fun provideTradeItemSmartCache(): SmartCache<String, CoinItem> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ExchangeAnnote
    fun provideExchangeSmartMap(): SmartMap<String, Coin> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ExchangeAnnote
    fun provideExchangeSmartCache(): SmartCache<String, Coin> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ExchangeItemAnnote
    fun provideExchangeItemSmartMap(): SmartMap<String, CoinItem> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ExchangeItemAnnote
    fun provideExchangeItemSmartCache(): SmartCache<String, CoinItem> {
        return SmartCache.newCache()
    }

    /* source */
    @Singleton
    @Provides
    @Room
    fun provideRoomCoinDataSource(
        mapper: CoinMapper,
        dao: CoinDao,
        quoteDao: QuoteDao
    ): CoinDataSource {
        return RoomCoinDataSource(mapper, dao, quoteDao)
    }

    @Singleton
    @Provides
    @Remote
    fun provideRemoteCoinDataSource(
        network: NetworkManager,
        keyM: KeyManager,
        mapper: CoinMapper,
        service: CoinMarketCapService
    ): CoinDataSource {
        return RemoteCoinDataSource(network, keyM, mapper, service)
    }

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
}