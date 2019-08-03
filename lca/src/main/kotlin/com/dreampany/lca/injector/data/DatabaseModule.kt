package com.dreampany.lca.injector.data

import android.app.Application
import com.dreampany.lca.data.source.dao.*
import com.dreampany.lca.data.source.room.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): DatabaseManager {
        return DatabaseManager.getInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideCoinDao(database: DatabaseManager): CoinDao {
        return database.coinDao()
    }

    @Singleton
    @Provides
    fun provideQuoteDao(database: DatabaseManager): QuoteDao {
        return database.quoteDao()
    }

    @Singleton
    @Provides
    fun providePriceDao(database: DatabaseManager): PriceDao {
        return database.priceDao()
    }

    @Singleton
    @Provides
    fun provideExchangeDao(database: DatabaseManager): ExchangeDao {
        return database.exchangeDao()
    }

    @Singleton
    @Provides
    fun provideMarketDao(database: DatabaseManager): MarketDao {
        return database.marketDao()
    }

    @Singleton
    @Provides
    fun provideChartDao(database: DatabaseManager): GraphDao {
        return database.graphDao()
    }

    @Singleton
    @Provides
    fun provideNewsDao(database: DatabaseManager): NewsDao {
        return database.newsDao()
    }

    @Singleton
    @Provides
    fun provideIcoDao(database: DatabaseManager): IcoDao {
        return database.icoDao()
    }

    @Singleton
    @Provides
    fun provideCoinAlertDao(database: DatabaseManager): CoinAlertDao {
        return database.coinAlertDao()
    }
}