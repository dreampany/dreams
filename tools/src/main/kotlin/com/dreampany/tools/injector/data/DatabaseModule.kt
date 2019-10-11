package com.dreampany.tools.injector.data

import android.app.Application
import com.dreampany.tools.data.source.room.dao.*
import com.dreampany.tools.data.source.room.database.*
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

    @Provides
    @Singleton
    fun providePointDatabase(application: Application): PointDatabase {
        return PointDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideNoteDatabase(application: Application): NoteDatabase {
        return NoteDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideWordDatabase(application: Application): WordDatabase {
        return WordDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideVpnDatabase(application: Application): VpnDatabase {
        return VpnDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideRadioDatabase(application: Application): RadioDatabase {
        return RadioDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun provideWordDao(database: WordDatabase): WordDao {
        return database.wordDao()
    }

    @Provides
    @Singleton
    fun provideSynonymDao(database: WordDatabase): SynonymDao {
        return database.synonymDao()
    }

    @Provides
    @Singleton
    fun provideAntonymDao(database: WordDatabase): AntonymDao {
        return database.antonymDao()
    }

    @Provides
    @Singleton
    fun provideServerDao(database: VpnDatabase): ServerDao {
        return database.serverDao()
    }

    @Provides
    @Singleton
    fun provideStationDao(database: RadioDatabase): StationDao {
        return database.stationDao()
    }
}