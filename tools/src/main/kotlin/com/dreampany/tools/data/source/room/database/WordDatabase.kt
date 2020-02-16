package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.data.model.word.Antonym
import com.dreampany.tools.data.model.word.Synonym
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.data.source.room.dao.AntonymDao
import com.dreampany.tools.data.source.room.dao.SynonymDao
import com.dreampany.tools.data.source.room.dao.WordDao
import com.dreampany.tools.data.source.room.converters.WordConverters
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Word::class, Synonym::class, Antonym::class], version = 1)
@TypeConverters(WordConverters::class)
abstract class WordDatabase : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.WORD)
        private var instance: WordDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): WordDatabase {
            val builder: RoomDatabase.Builder<WordDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, WordDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, WordDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): WordDatabase {
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

    abstract fun wordDao(): WordDao
    abstract fun synonymDao(): SynonymDao
    abstract fun antonymDao(): AntonymDao
}