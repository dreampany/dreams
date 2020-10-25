package com.dreampany.tube.inject.data

import com.dreampany.framework.inject.annote.Firestore
import com.dreampany.framework.inject.annote.Room
import com.dreampany.tube.data.source.api.SearchDataSource
import com.dreampany.tube.data.source.firestore.FirestoreManager
import com.dreampany.tube.data.source.firestore.SearchFirestoreDataSource
import com.dreampany.tube.data.source.mapper.SearchMapper
import com.dreampany.tube.data.source.room.SearchRoomDataSource
import com.dreampany.tube.data.source.room.dao.SearchDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 25/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class SearchModule {

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
        firestore: FirestoreManager
    ): SearchDataSource = SearchFirestoreDataSource(mapper, firestore)
}