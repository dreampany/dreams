package com.dreampany.lca.injector.data

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.lca.data.model.*
import com.dreampany.lca.misc.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
class SupportModule {

    @Singleton
    @Provides
    @CoinAnnote
    fun provideCoinSmartMap(): SmartMap<Long, Coin> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @CoinAlertAnnote
    fun provideCoinAlertSmartMap(): SmartMap<Long, CoinAlert> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @QuoteAnnote
    fun provideQuoteSmartMap(): SmartMap<Long, Quote> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @PriceAnnote
    fun providePriceSmartMap(): SmartMap<Long, Price> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ExchangeAnnote
    fun provideExchangeSmartMap(): SmartMap<Long, Exchange> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @MarketAnnote
    fun provideMarketSmartMap(): SmartMap<Long, Market> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @GraphAnnote
    fun provideGraphSmartMap(): SmartMap<Long, Graph> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @IcoAnnote
    fun provideIcoSmartMap(): SmartMap<Long, Ico> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @NewsAnnote
    fun provideNewsSmartMap(): SmartMap<Long, News> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @CoinAnnote
    fun provideCoinSmartCache(): SmartCache<Long, Coin> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @CoinAlertAnnote
    fun provideCoinAlertSmartCache(): SmartCache<Long, CoinAlert> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @QuoteAnnote
    fun provideQuoteSmartCache(): SmartCache<Long, Quote> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @PriceAnnote
    fun providePriceSmartCache(): SmartCache<Long, Price> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ExchangeAnnote
    fun provideExchangeSmartCache(): SmartCache<Long, Exchange> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @MarketAnnote
    fun provideMarketSmartCache(): SmartCache<Long, Market> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @GraphAnnote
    fun provideGraphSmartCache(): SmartCache<Long, Graph> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @IcoAnnote
    fun provideIcoSmartCache(): SmartCache<Long, Ico> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @NewsAnnote
    fun provideNewsSmartCache(): SmartCache<Long, News> {
        return SmartCache.newCache()
    }
}