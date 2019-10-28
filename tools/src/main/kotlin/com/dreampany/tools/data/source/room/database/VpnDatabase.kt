package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.room.converters.VpnConverters
import com.dreampany.tools.data.source.room.dao.ServerDao
import com.dreampany.tools.misc.Constants

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Server::class], version = 2)
@TypeConverters(VpnConverters::class)
abstract class VpnDatabase : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.VPN)
        private var instance: VpnDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): VpnDatabase {
            val builder: RoomDatabase.Builder<VpnDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, VpnDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(context, VpnDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): VpnDatabase {
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