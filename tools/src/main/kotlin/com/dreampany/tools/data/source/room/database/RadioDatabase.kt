package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.data.source.room.converters.RadioConverters
import com.dreampany.tools.data.source.room.dao.StationDao
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [StationDao::class], version = 1)
@TypeConverters(RadioConverters::class)
abstract class RadioDatabase : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.RADIO)
        private var instance: RadioDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): RadioDatabase {
            val builder: RoomDatabase.Builder<RadioDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, RadioDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, RadioDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): RadioDatabase {
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

    abstract fun stationDao(): StationDao
}