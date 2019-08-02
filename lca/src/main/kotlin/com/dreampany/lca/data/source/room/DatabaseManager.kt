package com.dreampany.lca.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.frame.BuildConfig
import com.dreampany.lca.data.model.*
import com.dreampany.lca.data.source.dao.*
import com.dreampany.lca.misc.Constants

/**
 * Created by Roman-372 on 8/2/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(
    entities = [
        Coin::class,
        Quote::class,
        Price::class,
        Exchange::class,
        Market::class,
        Graph::class,
        Ico::class,
        News::class,
        CoinAlert::class
    ], version = 16
)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.TYPE_LCA)
        private var instance: DatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager {
            val builder: Builder<DatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
            } else {
                builder = Room.databaseBuilder(context, DatabaseManager::class.java, DATABASE)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): DatabaseManager {
            if (instance == null) {
                instance = newInstance(context, false)
            }
            return instance!!
        }
    }

    abstract fun coinDao(): CoinDao

    abstract fun quoteDao(): QuoteDao

    abstract fun priceDao(): PriceDao

    abstract fun exchangeDao(): ExchangeDao

    abstract fun marketDao(): MarketDao

    abstract fun graphDao(): GraphDao

    abstract fun icoDao(): IcoDao

    abstract fun newsDao(): NewsDao

    abstract fun coinAlertDao(): CoinAlertDao
}