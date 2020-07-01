package com.dreampany.tube.data.source.remote

import android.content.Context
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tube.api.remote.service.YoutubeService
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.mapper.VideoMapper

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VideoRemoteDataSource(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: VideoMapper,
    private val service: YoutubeService
) : VideoDataSource {
    override suspend fun isFavorite(input: Video): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Video): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: Video): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Video>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): Video? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(query: String): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(ids: List<String>): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(offset: Long, limit: Long): List<Video>? {
        TODO("Not yet implemented")
    }
}