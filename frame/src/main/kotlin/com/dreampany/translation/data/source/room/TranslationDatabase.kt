package com.dreampany.translation.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dreampany.framework.BuildConfig
import com.dreampany.framework.misc.Constants
import com.dreampany.translation.data.model.TextTranslation

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [TextTranslation::class], version = 1)
abstract class TranslationDatabase : RoomDatabase() {

    companion object {
        private val DATABASE = Constants.database(BuildConfig.LIBRARY_PACKAGE_NAME, Constants.Database.TYPE_TRANSLATION)
        private var instance: TranslationDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): TranslationDatabase {
            val builder: Builder<TranslationDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, TranslationDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, TranslationDatabase::class.java, DATABASE)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): TranslationDatabase {
            if (instance == null) {
                instance = newInstance(context, false)
            }
            return instance!!
        }
    }

    abstract fun textTranslateDao(): TextTranslationDao
}