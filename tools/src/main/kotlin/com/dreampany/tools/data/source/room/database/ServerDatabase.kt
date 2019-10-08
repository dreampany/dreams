package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.room.converters.ServerConverters
import com.dreampany.tools.data.source.room.dao.ServerDao
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Server::class], version = 1)
@TypeConverters(ServerConverters::class)
abstract class ServerDatabase : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.SERVER)
        private var instance: ServerDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): ServerDatabase {
            val builder: RoomDatabase.Builder<ServerDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, ServerDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, ServerDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): ServerDatabase {
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

    abstract fun serverDao(): ServerDao
}