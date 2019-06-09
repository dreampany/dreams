package com.dreampany.lca.injector.data

import androidx.core.util.Pair
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.lca.data.enums.Currency
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
    fun provideCoinSmartMap(): SmartMap<String, Coin> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @CoinAlertAnnote
    fun provideCoinAlertSmartMap(): SmartMap<String, CoinAlert> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @QuoteAnnote
    fun provideQuoteSmartMap(): SmartMap<Pair<String, Currency>, Quote> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @PriceAnnote
    fun providePriceSmartMap(): SmartMap<String, Price> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @ExchangeAnnote
    fun provideExchangeSmartMap(): SmartMap<String, Exchange> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @MarketAnnote
    fun provideMarketSmartMap(): SmartMap<String, Market> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @GraphAnnote
    fun provideGraphSmartMap(): SmartMap<String, Graph> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @IcoAnnote
    fun provideIcoSmartMap(): SmartMap<String, Ico> {
        return SmartMap.newMap()
    }

    @Singleton
    @Provides
    @NewsAnnote
    fun provideNewsSmartMap(): SmartMap<String, News> {
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
    @CoinAlertAnnote
    fun provideCoinAlertSmartCache(): SmartCache<String, CoinAlert> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @QuoteAnnote
    fun provideQuoteSmartCache(): SmartCache<Pair<String, Currency>, Quote> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @PriceAnnote
    fun providePriceSmartCache(): SmartCache<String, Price> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @ExchangeAnnote
    fun provideExchangeSmartCache(): SmartCache<String, Exchange> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @MarketAnnote
    fun provideMarketSmartCache(): SmartCache<String, Market> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @GraphAnnote
    fun provideGraphSmartCache(): SmartCache<String, Graph> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @IcoAnnote
    fun provideIcoSmartCache(): SmartCache<String, Ico> {
        return SmartCache.newCache()
    }

    @Singleton
    @Provides
    @NewsAnnote
    fun provideNewsSmartCache(): SmartCache<String, News> {
        return SmartCache.newCache()
    }
}