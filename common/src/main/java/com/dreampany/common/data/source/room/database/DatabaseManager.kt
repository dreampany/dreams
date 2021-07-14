package com.dreampany.common.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.common.data.model.Store
import com.dreampany.common.data.model.Time
import com.dreampany.common.data.source.room.converter.Converter
import com.dreampany.common.data.source.room.dao.StoreDao
import com.dreampany.common.data.source.room.dao.TimeDao
import com.dreampany.common.misc.constant.Constant

/**
 * Created by roman on 7/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Database(entities = [Time::class, Store::class], version = 2, exportSchema = false)
@TypeConverters(Converter::class)
abstract class DatabaseManager : RoomDatabase() {
    companion object {
        private var instance: DatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager {
            val builder: Builder<DatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
            } else {
                val database = Constant.database(context, Constant.Room.TYPE_FRAMEWORK)
                builder = Room.databaseBuilder(context, DatabaseManager::class.java, database)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun instanceOf(context: Context): DatabaseManager {
            if (instance == null)
                instance = newInstance(context, false)
            return instance!!
        }
    }

    abstract fun timeDao(): TimeDao

    abstract fun storeDao(): StoreDao
}