package com.dreampany.word.injector.data

import android.app.Application
import com.dreampany.word.data.source.room.AntonymDao
import com.dreampany.word.data.source.room.DatabaseManager
import com.dreampany.word.data.source.room.SynonymDao
import com.dreampany.word.data.source.room.WordDao
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
    fun provideWordDao(database: DatabaseManager): WordDao {
        return database.wordDao()
    }

    @Singleton
    @Provides
    fun provideSynonymDao(database: DatabaseManager): SynonymDao {
        return database.synonymDao()
    }

    @Singleton
    @Provides
    fun provideAntonymDao(database: DatabaseManager): AntonymDao {
        return database.antonymDao()
    }
}