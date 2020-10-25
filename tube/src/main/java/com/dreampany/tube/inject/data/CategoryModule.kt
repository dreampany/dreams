package com.dreampany.tube.inject.data

import android.content.Context
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tube.api.remote.service.YoutubeService
import com.dreampany.tube.data.source.api.CategoryDataSource
import com.dreampany.tube.data.source.mapper.CategoryMapper
import com.dreampany.tube.data.source.remote.CategoryRemoteDataSource
import com.dreampany.tube.data.source.room.CategoryRoomDataSource
import com.dreampany.tube.data.source.room.dao.CategoryDao
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
class CategoryModule {

    @Singleton
    @Provides
    @Room
    fun provideCategoryRoomDataSource(
        mapper: CategoryMapper,
        dao: CategoryDao
    ): CategoryDataSource = CategoryRoomDataSource(mapper, dao)

    @Singleton
    @Provides
    @Remote
    fun provideCategoryRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: CategoryMapper,
        service: YoutubeService
    ): CategoryDataSource =
        CategoryRemoteDataSource(context, network, parser, keys, mapper, service)
}