package com.dreampany.translation.injector

import android.app.Application
import com.dreampany.translation.data.source.room.TextTranslationDao
import com.dreampany.translation.data.source.room.TranslationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideTranslateDatabase(application: Application): TranslationDatabase {
        return TranslationDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun provideTextTranslateDao(database: TranslationDatabase): TextTranslationDao{
        return database.textTranslateDao()
    }
}