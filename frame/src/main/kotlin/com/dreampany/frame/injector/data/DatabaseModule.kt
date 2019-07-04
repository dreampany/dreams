package com.dreampany.frame.injector.data

import android.app.Application
import com.dreampany.frame.data.source.StateDao
import com.dreampany.frame.data.source.StoreDao
import com.dreampany.frame.data.source.room.FrameDatabase
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
    fun provideFrameDatabase(application: Application): FrameDatabase {
        return FrameDatabase.onInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideStateDao(database: FrameDatabase): StateDao {
        return database.stateDao()
    }

    @Singleton
    @Provides
    fun provideStoreDao(database: FrameDatabase): StoreDao {
        return database.storeDao()
    }
}