package com.dreampany.tools.data.source.wifi.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.data.source.wifi.room.converters.Converters
import com.dreampany.tools.data.source.wifi.room.dao.WifiDao
import com.dreampany.tools.misc.constants.WifiConstants

/**
 * Created by roman on 24/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Wifi::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {

    companion object {
        private var instance: DatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager {
            val builder: Builder<DatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
            } else {
                val DATABASE = Constant.database(context, WifiConstants.Keys.Room.TYPE_WIFI)
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

    abstract fun wifiDao(): WifiDao
}