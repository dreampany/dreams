package com.dreampany.tools.injector.data

import android.app.Application
import com.dreampany.tools.data.source.dao.NoteDao
import com.dreampany.tools.data.source.dao.WordDao
import com.dreampany.tools.data.source.room.NoteDatabaseManager
import com.dreampany.tools.data.source.room.WordDatabaseManager
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
    fun provideNoteDatabase(application: Application): NoteDatabaseManager {
        return NoteDatabaseManager.getInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabaseManager): NoteDao {
        return database.noteDao()
    }

    @Singleton
    @Provides
    fun provideWordDatabase(application: Application): WordDatabaseManager {
        return WordDatabaseManager.getInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideWordDao(database: WordDatabaseManager): WordDao {
        return database.wordDao()
    }
}