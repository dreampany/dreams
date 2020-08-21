package com.dreampany.tube.data.source.room

import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.mapper.VideoMapper
import com.dreampany.tube.data.source.room.dao.VideoDao

/**
 * Created by roman on 1/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VideoRoomDataSource(
    private val mapper: VideoMapper,
    private val dao: VideoDao
) : VideoDataSource {

    @Throws
    override suspend fun isFavorite(input: Video): Boolean = mapper.isFavorite(input)

    @Throws
    override suspend fun toggleFavorite(input: Video): Boolean {
        val favorite = isFavorite(input)
        if (favorite) {
            mapper.deleteFavorite(input)
        } else {
            mapper.insertFavorite(input)
        }
        return favorite.not()
    }

    override suspend fun getFavorites(): List<Video>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun put(input: Video): Long {
        mapper.add(input)
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun put(inputs: List<Video>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(put(it)) }
        return result
    }

    @Throws
    override suspend fun putOfRegionCode(regionCode: String, inputs: List<Video>) {
        TODO("Not yet implemented")
    }

    override suspend fun putOfEvent(eventType: String, inputs: List<Video>) {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun putIf(inputs: List<Video>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach {
            if (!isExists(it.id))
                result.add(put(it))
        }
        return result
    }

    @Throws
    override suspend fun isExists(id: String): Boolean = dao.getCount(id) > 0

    override suspend fun get(id: String): Video? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(): List<Video>? {
        val result = dao.items
        return result
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

    @Throws
    override suspend fun getsOfCategoryId(categoryId: String): List<Video>? {
        val items = dao.getsOfCategoryId(categoryId)
        return items
    }

    @Throws
    override suspend fun getsOfCategoryId(
        categoryId: String,
        offset: Long,
        limit: Long
    ): List<Video>? = mapper.gets(categoryId, offset, limit, this)

    @Throws
    override suspend fun getsOfRegionCode(
        regionCode: String,
        offset: Long,
        limit: Long
    ): List<Video>? = mapper.getRegionVideos(regionCode)

    @Throws
    override suspend fun getsOfEvent(eventType: String, offset: Long, limit: Long): List<Video>? =
        mapper.getEventVideos(eventType)
}