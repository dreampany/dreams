package com.dreampany.framework.injector.data

import android.app.Application
import com.dreampany.framework.data.source.room.dao.StoreDao
import com.dreampany.framework.data.source.room.DatabaseManager
import com.dreampany.framework.data.source.room.dao.PointDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by roman on 2/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): DatabaseManager {
        return DatabaseManager.getInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideStoreDao(databaseManager: DatabaseManager): StoreDao {
        return databaseManager.storeDao()
    }

    @Singleton
    @Provides
    fun providePointDao(databaseManager: DatabaseManager): PointDao {
        return databaseManager.pointDao()
    }
}