package com.dreampany.tube.inject.data

import android.app.Application
import com.dreampany.framework.inject.data.DatabaseModule
import com.dreampany.tube.api.inject.data.YoutubeModule
import com.dreampany.tube.data.source.room.dao.*
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
        MiscModule::class,
        YoutubeModule::class,
        PageModule::class,
        CategoryModule::class,
        VideoModule::class
    ]
)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideCategoryDao(database: DatabaseManager): CategoryDao = database.categoryDao()

    @Provides
    @Singleton
    fun providePageDao(database: DatabaseManager): PageDao = database.pageDao()

    @Provides
    @Singleton
    fun provideVideoDao(database: DatabaseManager): VideoDao = database.videoDao()

    @Provides
    @Singleton
    fun provideRelatedDao(database: DatabaseManager): RelatedDao = database.relatedDao()
}