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
abstract class NoteDatabase : RoomDatabase() {

    companion object {
        private val DATABASE = Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.NOTE)
        private var instance: NoteDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): NoteDatabase {
            val builder: RoomDatabase.Builder<NoteDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, NoteDatabase::class.java, DATABASE)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance = newInstance(context, false)
            }
            return instance!!
        }
    }

    abstract fun noteDao(): NoteDao
}