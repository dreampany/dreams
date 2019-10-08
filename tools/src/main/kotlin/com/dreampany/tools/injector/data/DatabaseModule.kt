package com.dreampany.tools.injector.data

import android.app.Application
import com.dreampany.tools.data.source.room.dao.*
import com.dreampany.tools.data.source.room.database.NoteDatabase
import com.dreampany.tools.data.source.room.database.PointDatabase
import com.dreampany.tools.data.source.room.database.ServerDatabase
import com.dreampany.tools.data.source.room.database.WordDatabase
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
    fun providePointDatabase(application: Application): PointDatabase {
        return PointDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun provideNoteDatabase(application: Application): NoteDatabase {
        return NoteDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun provideWordDatabase(application: Application): WordDatabase {
        return WordDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun provideServerDatabase(application: Application): ServerDatabase {
        return ServerDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
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

    @Singleton
    @Provides
    fun provideServerDao(database: ServerDatabase): ServerDao {
        return database.serverDao()
    }
}