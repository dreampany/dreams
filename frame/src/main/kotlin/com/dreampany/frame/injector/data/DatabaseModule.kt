package com.dreampany.frame.injector.data

import android.app.Application
import com.dreampany.frame.data.source.dao.StoreDao
import com.dreampany.frame.data.source.room.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/21/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideFrameDatabase(application: Application): DatabaseManager {
        return DatabaseManager.getInstance(application.applicationContext)
    }

/*    @Singleton
    @Provides
    fun provideStateDao(database: DatabaseManager): StateDao {
        return database.stateDao()
    }*/

    @Singleton
    @Provides
    fun provideStoreDao(databaseManager: DatabaseManager): StoreDao {
        return databaseManager.storeDao()
    }
}