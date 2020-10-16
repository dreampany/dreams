package com.dreampany.tube.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.model.Related
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.room.converters.Converters
import com.dreampany.tube.data.source.room.dao.CategoryDao
import com.dreampany.tube.data.source.room.dao.RelatedDao
import com.dreampany.tube.data.source.room.dao.VideoDao
import com.dreampany.tube.misc.Constants

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Category::class, Video::class, Related::class], version = 3, exportSchema = false)
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
                val DATABASE = Constant.database(context, Constants.Keys.Room.TYPE_TUBE)
                builder = Room.databaseBuilder(context, DatabaseManager::class.java, DATABASE)
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

    abstract fun categoryDao(): CategoryDao

    abstract fun videoDao(): VideoDao

    abstract fun relatedDao(): RelatedDao
}