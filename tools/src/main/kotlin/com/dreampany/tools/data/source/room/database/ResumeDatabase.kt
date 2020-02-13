package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.data.model.resume.Resume
import com.dreampany.tools.data.source.room.converters.ResumeConverters
import com.dreampany.tools.data.source.room.dao.ResumeDao
import com.dreampany.tools.misc.Constants

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Resume::class], version = 1)
@TypeConverters(ResumeConverters::class)
abstract
class ResumeDatabase : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.RESUME)
        private var instance: ResumeDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): ResumeDatabase {
            val builder: RoomDatabase.Builder<ResumeDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, ResumeDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, ResumeDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): ResumeDatabase {
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

    abstract fun resumeDao(): ResumeDao
}