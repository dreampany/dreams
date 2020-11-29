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
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(
    entities = [
        Category::class,
        Page::class,
        Video::class,
        Related::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {

    companion object {
        private var instance: DatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager {
            val builder: RoomDatabase.Builder<DatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
            } else {
                val database = Constant.database(context, Constants.Keys.Room.ROOM)
                builder = Room.databaseBuilder(context, DatabaseManager::class.java, database)
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

    abstract fun pageDao(): PageDao
    abstract fun categoryDao(): CategoryDao
    abstract fun videoDao(): VideoDao
    abstract fun relatedDao(): RelatedDao
}