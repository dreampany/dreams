package com.dreampany.common.inject.data

import com.dreampany.common.data.source.api.TimeDataSource
import com.dreampany.common.data.source.room.TimeRoomDataSource
import com.dreampany.common.data.source.room.dao.TimeDao
import com.dreampany.common.inject.qualifier.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by roman on 7/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Module
@InstallIn(SingletonComponent::class)
object TimeModule {

    @Room
    @Provides
    fun room(
        dao: TimeDao
    ): TimeDataSource = TimeRoomDataSource(dao)
}