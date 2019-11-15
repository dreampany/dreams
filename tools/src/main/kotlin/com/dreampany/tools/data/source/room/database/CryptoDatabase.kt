package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.model.Quote
import com.dreampany.tools.data.source.room.converters.CryptoConverters
import com.dreampany.tools.data.source.room.dao.CoinDao
import com.dreampany.tools.data.source.room.dao.QuoteDao
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Coin::class, Quote::class], version = 1)
@TypeConverters(CryptoConverters::class)
abstract class CryptoDatabase : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.CRYPTO)
        private var instance: CryptoDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): CryptoDatabase {
            val builder: RoomDatabase.Builder<CryptoDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, CryptoDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(
                    context, CryptoDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): CryptoDatabase {
            if (instance == null) {
                instance =
                    newInstance(
                        context,
                        false
                    )
            }
            return instance!!
        }
    }

    abstract fun coinDao(): CoinDao
    abstract fun quoteDao(): QuoteDao
}