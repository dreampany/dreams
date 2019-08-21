package com.dreampany.frame.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.frame.BuildConfig
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.data.source.dao.StoreDao
import com.dreampany.frame.misc.Constants

/**
 * Created by Roman-372 on 7/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Store::class], version = 1)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {

    companion object {
        private val DATABASE = Constants.database(BuildConfig.APPLICATION_ID)
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

    abstract fun storeDao(): StoreDao
}