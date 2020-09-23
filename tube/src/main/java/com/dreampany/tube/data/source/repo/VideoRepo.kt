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
import timber.log.Timber
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

    @kotlin.jvm.Throws
    override suspend fun getFavorites() = withContext(Dispatchers.IO) {
        room.getFavorites()
    }

    override suspend fun put(input: Video): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Video>): List<Long>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun putOfRegionCode(regionCode: String, inputs: List<Video>) {
        TODO("Not yet implemented")
    }

    override suspend fun putOfEvent(eventType: String, inputs: List<Video>) {
        TODO("Not yet implemented")
    }

    override suspend fun putIf(inputs: List<Video>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun isExists(id: String): Boolean {
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

    @Throws
    override suspend fun getsOfQuery(query: String, offset: Long, limit: Long) =
        withContext(Dispatchers.IO) {
            remote.getsOfQuery(query, offset, limit)
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
                room.putIf(result)
                mapper.commitExpire(categoryId, offset)
                result.expiredIds()?.let {
                    if (it.isNotEmpty()) {
                        result = remote.gets(it)
                        result?.let {
                            val puts = room.put(it)
                            Timber.v("")
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

    @Throws
    override suspend fun getsOfRegionCode(
        regionCode: String,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(regionCode, offset)) {
            var result = remote.getsOfRegionCode(regionCode, offset, limit)
            if (!result.isNullOrEmpty()) {
                room.putIf(result)
                mapper.setRegionVideos(regionCode, result)
                mapper.commitExpire(regionCode, offset)
                result.expiredIds()?.let {
                    if (it.isNotEmpty()) {
                        result = remote.gets(it)
                        result?.let {
                            val puts = room.put(it)
                            Timber.v("")
                        }
                        result?.forEach {
                            mapper.commitExpire(it.id)
                        }
                    }
                }
            }
        }
        room.getsOfRegionCode(regionCode, offset, limit)
    }

    @Throws
    override suspend fun getsOfLocation(
        location: String,
        radius: String,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        remote.getsOfLocation(location, radius, offset, limit)
    }

    @Throws
    override suspend fun getsOfEvent(eventType: String, offset: Long, limit: Long) =
        withContext(Dispatchers.IO) {
            if (mapper.isExpired(eventType, offset)) {
                var result = remote.getsOfEvent(eventType, offset, limit)
                if (!result.isNullOrEmpty()) {
                    room.putIf(result)
                    mapper.setEventVideos(eventType, result)
                    mapper.commitExpire(eventType, offset)
                    result.expiredIds()?.let {
                        if (it.isNotEmpty()) {
                            result = remote.gets(it)
                            result?.let {
                                val puts = room.put(it)
                                Timber.v("")
                            }
                            result?.forEach {
                                mapper.commitExpire(it.id)
                            }
                        }
                    }
                }
            }
            room.getsOfEvent(eventType, offset, limit)
        }

    @Throws
    override suspend fun getsOfRelated(id: String, offset: Long, limit: Long) =
        withContext(Dispatchers.IO) {
            var result: List<Video>? = null
            if (mapper.isExpiredOfRelated(id)) {
                result = remote.getsOfRelated(id, offset, limit)
                if (!result.isNullOrEmpty()) {
                    room.putIf(result)
                    mapper.commitExpireOfRelated(id)
                    result.expiredIds()?.let {
                        if (it.isNotEmpty()) {
                            result = remote.gets(it)
                            result?.let {
                                val puts = room.put(it)
                                Timber.v("")
                            }
                            result?.forEach {
                                mapper.commitExpire(it.id)
                            }
                        }
                    }
                }
            }
            if (result.isNullOrEmpty()) {
                result = room.getsOfRelated(id, offset, limit)
            }
            result
        }


    suspend fun List<Video>.expiredIds(): List<String>? =
        this.filter { mapper.isExpired(it.id) }.map { it.id }
}