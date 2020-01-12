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
    fun provideCryptoDatabase(application: Application): CryptoDatabase {
        return CryptoDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideContactDatabase(application: Application): ContactDatabase {
        return ContactDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideResumeDatabase(application: Application): ResumeDatabase {
        return ResumeDatabase.getInstance(application)
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

    @Provides
    @Singleton
    fun provideCoinDao(database: CryptoDatabase): CoinDao {
        return database.coinDao()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(database: CryptoDatabase): QuoteDao {
        return database.quoteDao()
    }

    @Provides
    @Singleton
    fun provideContactDao(database: ContactDatabase): ContactDao {
        return database.contactDao()
    }

    @Provides
    @Singleton
    fun provideResumeDao(database: ResumeDatabase): ResumeDao {
        return database.resumeDao()
    }
}