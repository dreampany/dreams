package com.dreampany.history.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.frame.BuildConfig
import com.dreampany.history.data.model.ImageLink
import com.dreampany.history.data.model.History
import com.dreampany.history.data.source.dao.HistoryDao
import com.dreampany.history.data.source.dao.ImageLinkDao
import com.dreampany.history.misc.Constants

/**
 * Created by Roman-372 on 7/25/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [History::class, ImageLink::class], version = 3)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.TYPE_HISTORY)
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

    abstract fun historyDao(): HistoryDao
    abstract fun imageLinkDao(): ImageLinkDao
}