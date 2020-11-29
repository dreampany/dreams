package com.dreampany.tube.data.source.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.api.VideoDataSource
import com.dreampany.tube.data.source.mapper.VideoMapper
import com.dreampany.tube.data.source.pref.Prefs
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
    private val pref: Prefs,
    private val mapper: VideoMapper,
    @Room private val room: VideoDataSource,
    @Remote private val remote: VideoDataSource
) : VideoDataSource {

    @Throws
    @Synchronized
    override suspend fun isFavorite(input: Video) = withContext(Dispatchers.IO) {
        room.isFavorite(input)
    }

    @Throws
    @Synchronized
    override suspend fun toggleFavorite(input: Video): Boolean = withContext(Dispatchers.IO) {
        room.toggleFavorite(input)
    }

    @Throws
    @Synchronized
    override suspend fun favorites() = withContext(Dispatchers.IO) {
        room.favorites()
    }

    @Throws
    @Synchronized
    override suspend fun writeRecent(input: Video): Boolean = withContext(Dispatchers.IO) {
        room.writeRecent(input)
    }

    @Throws
    @Synchronized
    override suspend fun recents(): List<Video>? = withContext(Dispatchers.IO) {
        room.favorites()
    }

    override suspend fun write(input: Video): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<Video>): List<Long>? {
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
    override suspend fun getsOfQuery(query: String, order: String, offset: Long, limit: Long) =
        withContext(Dispatchers.IO) {
            remote.getsOfQuery(query, order, offset, limit)
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
                mapper.writeExpire(categoryId, offset)
                result.expiredIds()?.let {
                    if (it.isNotEmpty()) {
                        result = remote.gets(it)
                        result?.let {
                            val puts = room.write(it)
                            Timber.v("")
                        }
                        result?.forEach {
                            mapper.writeExpire(it.id)
                        }
                    }
                }
            }
        }
        room.getsOfCategoryId(categoryId, offset, limit)
    }

    @Throws
    override suspend fun getsOfRegionCode(
        regionCode: String, order: String,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        if (mapper.isExpired(regionCode, offset)) {
            var result = remote.getsOfRegionCode(regionCode, order, offset, limit)
            if (!result.isNullOrEmpty()) {
                room.putIf(result)
                mapper.setRegionVideos(regionCode, result)
                mapper.writeExpire(regionCode, offset)
                result.expiredIds()?.let {
                    if (it.isNotEmpty()) {
                        result = remote.gets(it)
                        result?.let {
                            val puts = room.write(it)
                        }
                        result?.forEach {
                            mapper.writeExpire(it.id)
                        }
                    }
                }
            }
        }
        room.getsOfRegionCode(regionCode, order, offset, limit)
    }

    @Throws
    override suspend fun getsOfLocation(
        location: String,
        radius: String, order: String,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        remote.getsOfLocation(location, radius, order, offset, limit)
    }

    @Throws
    override suspend fun getsOfEvent(eventType: String, order: String, offset: Long, limit: Long) =
        withContext(Dispatchers.IO) {
            if (mapper.isExpired(eventType, offset)) {
                var result = remote.getsOfEvent(eventType, order, offset, limit)
                if (!result.isNullOrEmpty()) {
                    room.putIf(result)
                    mapper.setEventVideos(eventType, result)
                    mapper.writeExpire(eventType, offset)
                    result.expiredIds()?.let {
                        if (it.isNotEmpty()) {
                            result = remote.gets(it)
                            result?.let {
                                val puts = room.write(it)
                                Timber.v("")
                            }
                            result?.forEach {
                                mapper.writeExpire(it.id)
                            }
                        }
                    }
                }
            }
            room.getsOfEvent(eventType, order, offset, limit)
        }

    @Throws
    override suspend fun getsOfRelated(id: String, order: String, offset: Long, limit: Long) =
        withContext(Dispatchers.IO) {
            var result: List<Video>? = null
            if (mapper.isExpiredOfRelated(id)) {
                result = remote.getsOfRelated(id, order, offset, limit)
                if (!result.isNullOrEmpty()) {
                    room.putIf(result)
                    mapper.commitExpireOfRelated(id)
                    result.expiredIds()?.let {
                        if (it.isNotEmpty()) {
                            result = remote.gets(it)
                            result?.let {
                                val puts = room.write(it)
                                Timber.v("")
                            }
                            result?.forEach {
                                mapper.writeExpire(it.id)
                            }
                        }
                    }
                }
            }
            if (result.isNullOrEmpty()) {
                result = room.getsOfRelated(id, order, offset, limit)
            }
            result
        }


    suspend fun List<Video>.expiredIds(): List<String>? =
        this.filter { mapper.isExpired(it.id) }.map { it.id }
}