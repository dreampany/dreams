package com.dreampany.tools.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.frame.BuildConfig
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.dao.WordDao
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Word::class], version = 1)
@TypeConverters(WordConverters::class)
abstract class WordDatabaseManager : RoomDatabase() {

    companion object {
        private val DATABASE = Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.WORD)
        private var instance: WordDatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): WordDatabaseManager {
            val builder: RoomDatabase.Builder<WordDatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, WordDatabaseManager::class.java)
            } else {
                builder = Room.databaseBuilder(context, WordDatabaseManager::class.java, DATABASE)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): WordDatabaseManager {
            if (instance == null) {
                instance = newInstance(context, false)
            }
            return instance!!
        }
    }

    abstract fun wordDao(): WordDao
}