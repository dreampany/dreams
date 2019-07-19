package com.dreampany.frame.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dreampany.frame.BuildConfig
import com.dreampany.frame.data.model.State
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.data.source.dao.StateDao
import com.dreampany.frame.data.source.dao.StoreDao
import com.dreampany.frame.misc.Constants

/**
 * Created by Roman-372 on 7/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [State::class, Store::class], version = 10)
abstract class FrameDatabase : RoomDatabase() {

    companion object {
        private val DATABASE = Constants.database(BuildConfig.APPLICATION_ID)
        private var instance: FrameDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): FrameDatabase {
            val builder: Builder<FrameDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, FrameDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, FrameDatabase::class.java,
                    FrameDatabase.DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): FrameDatabase {
            if (instance == null) {
                instance = newInstance(context, false)
            }
            return instance!!
        }
    }

    abstract fun stateDao(): StateDao

    abstract fun storeDao(): StoreDao
}