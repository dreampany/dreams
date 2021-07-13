package com.dreampany.common.inject.data

import android.content.Context
import com.dreampany.common.data.source.room.dao.StoreDao
import com.dreampany.common.data.source.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by roman on 7/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DatabaseManager =
        DatabaseManager.instanceOf(context)

    @Provides
    @Singleton
    fun provideStoreDao(database: DatabaseManager): StoreDao = database.storeDao()
}