package com.dreampany.history.injector.data

import android.app.Application
import com.dreampany.history.data.source.room.DatabaseManager
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
        return DatabaseManager.getInstance(application.applicationContext)
    }
}