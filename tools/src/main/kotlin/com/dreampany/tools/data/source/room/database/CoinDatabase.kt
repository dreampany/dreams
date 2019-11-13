package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.BuildConfig
import com.dreampany.framework.data.source.room.converters.Converters
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.source.room.dao.CoinDao
import com.dreampany.tools.data.source.room.dao.NoteDao
import com.dreampany.tools.data.source.room.dao.QuoteDao
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Coin::class], version = 1)
@TypeConverters(Converters::class)
abstract class CoinDatabase : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.COIN)
        private var instance: CoinDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): CoinDatabase {
            val builder: RoomDatabase.Builder<CoinDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, CoinDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(
                    context, CoinDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): CoinDatabase {
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