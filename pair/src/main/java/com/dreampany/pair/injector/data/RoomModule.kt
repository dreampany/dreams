package com.dreampany.pair.injector.data

import android.app.Application
import com.dreampany.pair.data.source.room.dao.UserDao
import com.dreampany.pair.data.source.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager {
        return DatabaseManager.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideUserDao(database: DatabaseManager): UserDao {
        return database.userDao()
    }
}