package com.dreampany.tube.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.tube.data.model.*
import com.dreampany.tube.data.source.room.converters.Converters
import com.dreampany.tube.data.source.room.dao.*
import com.dreampany.tube.misc.Constants

/**
 * Created by roman on 26/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(
    entities = [
        Search::class,
        Category::class,
        Page::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DatabaseLite : RoomDatabase() {

    companion object {
        private var instance: DatabaseLite? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseLite {
            val builder: Builder<DatabaseLite>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseLite::class.java)
            } else {
                val database = Constant.database(context, Constants.Keys.Room.TYPE_LITE)
                builder = Room.databaseBuilder(context, DatabaseLite::class.java, database)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): DatabaseLite {
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

    abstract fun searchDao(): SearchDao
    abstract fun categoryDao(): CategoryDao
    abstract fun pageDao(): PageDao
}