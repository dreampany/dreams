package com.dreampany.radio.inject.data

import android.app.Application
import com.dreampany.radio.data.source.misc.api.SearchDataSource
import com.dreampany.radio.data.source.misc.firestore.SearchFirestoreDataSource
import com.dreampany.radio.data.source.misc.mapper.SearchMapper
import com.dreampany.radio.data.source.misc.room.SearchRoomDataSource
import com.dreampany.radio.data.source.misc.room.dao.SearchDao
import com.dreampany.radio.data.source.misc.room.database.DatabaseManager
import com.dreampany.radio.manager.AuthManager
import com.dreampany.radio.manager.FirestoreManager
import com.dreampany.framework.inject.annote.Firestore
import com.dreampany.framework.inject.annote.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 26/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class MiscModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): DatabaseManager =
        DatabaseManager.getInstance(application)

    @Provides
    @Singleton
    fun provideSearchDao(database: DatabaseManager): SearchDao = database.searchDao()

    @Singleton
    @Provides
    @Room
    fun provideSearchRoomDataSource(
        dao: SearchDao
    ): SearchDataSource = SearchRoomDataSource(dao)

    @Singleton
    @Provides
    @Firestore
    fun provideSearchFirestoreDataSource(
        mapper: SearchMapper,
        auth: AuthManager,
        firestore: FirestoreManager
    ): SearchDataSource = SearchFirestoreDataSource(mapper, auth, firestore)
}