package com.dreampany.tools.injector.data

import android.app.Application
import com.dreampany.tools.data.source.dao.AntonymDao
import com.dreampany.tools.data.source.dao.NoteDao
import com.dreampany.tools.data.source.dao.SynonymDao
import com.dreampany.tools.data.source.dao.WordDao
import com.dreampany.tools.data.source.room.NoteDatabase
import com.dreampany.tools.data.source.room.WordDatabase
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
    fun provideNoteDatabase(application: Application): NoteDatabase {
        return NoteDatabase.getInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }

    @Singleton
    @Provides
    fun provideWordDatabase(application: Application): WordDatabase {
        return WordDatabase.getInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideWordDao(database: WordDatabase): WordDao {
        return database.wordDao()
    }

    @Singleton
    @Provides
    fun provideSynonymDao(database: WordDatabase): SynonymDao {
        return database.synonymDao()
    }

    @Singleton
    @Provides
    fun provideAntonymDao(database: WordDatabase): AntonymDao {
        return database.antonymDao()
    }
}