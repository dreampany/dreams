package com.dreampany.tools.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.tools.data.model.Contact
import com.dreampany.tools.data.source.room.converters.ContactConverters
import com.dreampany.tools.data.source.room.dao.ContactDao
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.BuildConfig

/**
 * Created by Roman-372 on 8/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Contact::class], version = 1)
@TypeConverters(ContactConverters::class)
abstract class ContactDatabase : RoomDatabase() {

    companion object {
        private val DATABASE =
            Constants.database(BuildConfig.APPLICATION_ID, Constants.Database.CONTACT)
        private var instance: ContactDatabase? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): ContactDatabase {
            val builder: RoomDatabase.Builder<ContactDatabase>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, ContactDatabase::class.java)
            } else {
                builder = Room.databaseBuilder(
                    context, ContactDatabase::class.java,
                    DATABASE
                )
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): ContactDatabase {
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

    abstract fun contactDao(): ContactDao
}