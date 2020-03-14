package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.data.source.room.converters.QuestionConverters
import com.dreampany.tools.data.source.room.dao.QuestionDao
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Question::class], version = 1)
@TypeConverters(QuestionConverters::class)
abstract class QuestionDatabase: RoomDatabase() {

    companion object {
        private val DATABASE = Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.QUESTION)
        private var instance: QuestionDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): QuestionDatabase {
            val builder: RoomDatabase.Builder<QuestionDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, QuestionDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, QuestionDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): QuestionDatabase {
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

    abstract fun questionDao(): QuestionDao
}