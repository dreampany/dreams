package com.dreampany.tube.data.source.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.mapper.VideoMapper
import com.dreampany.tube.data.source.pref.AppPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class VideoRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: AppPref,
    private val mapper: VideoMapper,
    @Room private val room: VideoDataSource,
    @Remote private val remote: VideoDataSource
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

    override suspend fun gets(ids: List<String>): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(offset: Long, limit: Long): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun getsOfQuery(query: String): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun getsOfCategoryId(categoryId: String): List<Video>? {
        TODO("Not yet implemented")
    }

    override suspend fun getsOfCategoryId(
        categoryId: String,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(categoryId, offset)) {
            val result = remote.getsOfCategoryId(categoryId)
            if (!result.isNullOrEmpty()) {
                val result = room.put(result)
                if (!result.isNullOrEmpty()) {
                    mapper.commitExpire(categoryId, offset)
                }
            }
        }
        room.getsOfCategoryId(categoryId, offset, limit)
    }

}