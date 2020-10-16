package com.dreampany.crypto.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.crypto.data.model.Article
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.data.model.Quote
import com.dreampany.crypto.data.source.room.converters.Converters
import com.dreampany.crypto.data.source.room.dao.CoinDao
import com.dreampany.crypto.data.source.room.dao.ArticleDao
import com.dreampany.crypto.data.source.room.dao.QuoteDao
import com.dreampany.crypto.misc.constants.Constants
import com.dreampany.framework.misc.constant.Constant

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Coin::class, Quote::class, Article::class], version = 4, exportSchema = false)
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
                val DATABASE = Constant.database(context, Constants.Keys.Room.TYPE_CRYPTO)
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

    abstract fun articleDao(): ArticleDao
}