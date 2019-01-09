package com.dreampany.radio.injector.data

import android.app.Application
import com.dreampany.radio.data.source.room.DatabaseManager
import com.dreampany.radio.data.source.room.StationDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/11/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): DatabaseManager {
        return DatabaseManager.onInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideStationDao(database: DatabaseManager): StationDao {
        return database.stationDao()
    }
}