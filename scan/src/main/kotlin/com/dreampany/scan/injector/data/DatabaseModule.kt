package com.dreampany.scan.injector.data

import android.app.Application
import com.dreampany.frame.data.source.local.FlagDao
import com.dreampany.frame.data.source.local.FrameDatabase
import com.dreampany.scan.data.source.local.DatabaseManager
import com.dreampany.scan.data.source.local.ScanDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
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
    fun provideDatabase(application: Application): DatabaseManager {
        return DatabaseManager.onInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideFlagDao(database: FrameDatabase): FlagDao {
        return database.flagDao()
    }

    @Singleton
    @Provides
    fun provideScanDao(database: DatabaseManager): ScanDao {
        return database.scanDao()
    }
}