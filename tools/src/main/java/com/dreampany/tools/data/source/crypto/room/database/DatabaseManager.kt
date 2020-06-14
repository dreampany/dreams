package com.dreampany.tools.data.source.crypto.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.room.converters.Converters
import com.dreampany.tools.data.source.crypto.room.dao.CoinDao
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao
import com.dreampany.tools.misc.constants.CryptoConstants

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Coin::class, Quote::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {

    companion object {
        private var instance: DatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager {
            val builder: RoomDatabase.Builder<DatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
            } else {
                val DATABASE = Constants.database(context, CryptoConstants.Keys.Room.TYPE_CRYPTO)
                builder = Room.databaseBuilder(context, DatabaseManager::class.java, DATABASE)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): DatabaseManager {
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