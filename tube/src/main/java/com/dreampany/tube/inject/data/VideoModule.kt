package com.dreampany.tube.inject.data

import android.content.Context
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tube.api.remote.service.YoutubeService
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.mapper.VideoMapper
import com.dreampany.tube.data.source.remote.VideoRemoteDataSource
import com.dreampany.tube.data.source.room.VideoRoomDataSource
import com.dreampany.tube.data.source.room.dao.RelatedDao
import com.dreampany.tube.data.source.room.dao.VideoDao
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
class VideoModule {
    @Singleton
    @Provides
    @Room
    fun provideVideoRoomDataSource(
        mapper: VideoMapper,
        dao: VideoDao,
        relatedDao: RelatedDao
    ): VideoDataSource = VideoRoomDataSource(mapper, dao, relatedDao)

    @Singleton
    @Provides
    @Remote
    fun provideVideoRemoteDataSource(
        context: Context,
        network: NetworkManager,
        parser: Parser,
        keys: Keys,
        mapper: VideoMapper,
        service: YoutubeService
    ): VideoDataSource = VideoRemoteDataSource(context, network, parser, keys, mapper, service)
}