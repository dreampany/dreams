package com.dreampany.frame.injector.data

import android.app.Application
import com.dreampany.frame.data.source.local.FlagDao
import com.dreampany.frame.data.source.room.FrameDatabase
import com.dreampany.frame.data.source.local.StateDao
import com.dreampany.frame.data.source.local.StoreDao
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
    fun provideFlagDao(database: FrameDatabase): FlagDao {
        return database.flagDao()
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