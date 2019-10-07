package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.data.source.room.converters.PointConverters
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2019-09-01
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@TypeConverters(PointConverters::class)
abstract class PointDatabase : RoomDatabase() {
    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.POINT)
        private var instance: PointDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): PointDatabase {
            val builder: RoomDatabase.Builder<PointDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, PointDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(
                    context, PointDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): PointDatabase {
            if (instance == null) {
                instance = newInstance(context, false)
            }
            return instance!!
        }
    }
}