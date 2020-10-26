package com.dreampany.tube.inject.data

import android.app.Application
import com.dreampany.framework.inject.data.DatabaseModule
import com.dreampany.tube.api.inject.data.YoutubeModule
import com.dreampany.tube.data.source.room.dao.*
import com.dreampany.tube.data.source.room.database.DatabaseLite
import com.dreampany.tube.data.source.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 5/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module(
    includes = [
        DatabaseModule::class,
        YoutubeModule::class,
        SearchModule::class,
        PageModule::class,
        CategoryModule::class,
        VideoModule::class
    ]
)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabaseLite(application: Application): DatabaseLite =
        DatabaseLite.getInstance(application)

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideSearchDao(database: DatabaseLite): SearchDao = database.searchDao()

    @Provides
    @Singleton
    fun provideCategoryDao(database: DatabaseLite): CategoryDao = database.categoryDao()

    @Provides
    @Singleton
    fun providePageDao(database: DatabaseLite): PageDao = database.pageDao()

    @Provides
    @Singleton
    fun provideVideoDao(database: DatabaseManager): VideoDao = database.videoDao()

    @Provides
    @Singleton
    fun provideRelatedDao(database: DatabaseManager): RelatedDao = database.relatedDao()
}