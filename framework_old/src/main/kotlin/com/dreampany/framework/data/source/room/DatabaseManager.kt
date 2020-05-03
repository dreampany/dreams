package com.dreampany.framework.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.framework.BuildConfig
import com.dreampany.framework.data.model.Point
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.source.room.converters.Converters
import com.dreampany.framework.data.source.room.dao.PointDao
import com.dreampany.framework.data.source.room.dao.StoreDao
import com.dreampany.framework.misc.Constants

/**
 * Created by Roman-372 on 7/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Store::class, Point::class], version = 2)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {

    companion object {
        private val DATABASE = Constants.database(BuildConfig.LIBRARY_PACKAGE_NAME)
        private var instance: DatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager {
            val builder: Builder<DatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
            } else {
                builder = Room.databaseBuilder(context, DatabaseManager::class.java,
                    DATABASE
                )
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

    abstract fun storeDao(): StoreDao

    abstract fun pointDao(): PointDao
}