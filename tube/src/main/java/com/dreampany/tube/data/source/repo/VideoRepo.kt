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

    @Throws
    override suspend fun isFavorite(input: Video) = withContext(Dispatchers.IO) {
        room.isFavorite(input)
    }

    @Throws
    override suspend fun toggleFavorite(input: Video) = withContext(Dispatchers.IO) {
        room.toggleFavorite(input)
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

    @Throws
    override suspend fun getsOfCategoryId(
        categoryId: String,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(categoryId, offset)) {
            var result = remote.getsOfCategoryId(categoryId, offset, limit)
            if (!result.isNullOrEmpty()) {
                room.put(result)
                mapper.commitExpire(categoryId, offset)
                result.expiredIds?.let {
                    if (it.isNotEmpty()) {
                        result = remote.gets(it)
                        result?.let {
                            room.put(it)
                        }
                        result?.forEach {
                            mapper.commitExpire(it.id)
                        }
                    }
                }
            }
        }
        room.getsOfCategoryId(categoryId, offset, limit)
    }


    private val List<Video>.expiredIds: List<String>?
        get() = this.filter { mapper.isExpired(it.id) }.map { it.id }
}