package com.dreampany.tube.inject.data

import android.content.Context
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.data.StoreModule
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tube.api.inject.data.YoutubeModule
import com.dreampany.tube.api.remote.service.YoutubeService
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.mapper.VideoMapper
import com.dreampany.tube.data.source.remote.VideoRemoteDataSource
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
        StoreModule::class,
        YoutubeModule::class
    ]
)
class DataModule {
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