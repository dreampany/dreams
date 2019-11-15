package com.dreampany.tools.injector.data

import androidx.core.util.Pair
import com.dreampany.framework.api.key.KeyManager
import com.dreampany.framework.misc.Remote
import com.dreampany.framework.misc.Room
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.injector.CoinMarketCapModule
import com.dreampany.tools.api.crypto.remote.CoinMarketCapService
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.mapper.CoinMapper
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.model.Quote
import com.dreampany.tools.data.source.api.CoinDataSource
import com.dreampany.tools.data.source.remote.RemoteCoinDataSource
import com.dreampany.tools.data.source.room.RoomCoinDataSource
import com.dreampany.tools.data.source.room.dao.CoinDao
import com.dreampany.tools.data.source.room.dao.QuoteDao
import com.dreampany.tools.injector.annotation.CoinAnnote
import com.dreampany.tools.injector.annotation.CoinItemAnnote
import com.dreampany.tools.injector.annotation.CurrencyAnnote
import com.dreampany.tools.injector.annotation.QuoteAnnote
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
        CoinMarketCapModule::class
    ]
)
class CryptoModule {

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
}