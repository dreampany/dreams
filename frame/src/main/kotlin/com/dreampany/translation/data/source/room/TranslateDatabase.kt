package com.dreampany.translation.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dreampany.frame.BuildConfig
import com.dreampany.frame.misc.Constants
import com.dreampany.translation.data.model.TextTranslation

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [TextTranslation::class], version = 1)
abstract class TranslateDatabase : RoomDatabase() {

    companion object {
        private val DATABASE = Constants.database(BuildConfig.APPLICATION_ID)
        private var instance: TranslateDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): TranslateDatabase {
            val builder: Builder<TranslateDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, TranslateDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, TranslateDatabase::class.java, DATABASE)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): TranslateDatabase {
            if (instance == null) {
                instance = newInstance(context, false)
            }
            return instance!!
        }
    }

    abstract fun textTranslateDao(): TextTranslationDao
}