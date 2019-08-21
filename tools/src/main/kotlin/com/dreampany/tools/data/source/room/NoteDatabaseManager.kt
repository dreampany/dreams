package com.dreampany.tools.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.frame.BuildConfig
import com.dreampany.frame.data.source.room.Converters
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.source.dao.NoteDao
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Note::class], version = 1)
@TypeConverters(Converters::class)
abstract class NoteDatabaseManager : RoomDatabase() {

    companion object {
        private val DATABASE = Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.NOTE)
        private var instance: NoteDatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): NoteDatabaseManager {
            val builder: RoomDatabase.Builder<NoteDatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, NoteDatabaseManager::class.java)
            } else {
                builder = Room.databaseBuilder(context, NoteDatabaseManager::class.java, DATABASE)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): NoteDatabaseManager {
            if (instance == null) {
                instance = newInstance(context, false)
            }
            return instance!!
        }
    }

    abstract fun noteDao(): NoteDao
}