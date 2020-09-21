package com.dreampany.tube.data.source.api

import com.dreampany.tube.data.model.Video

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface VideoDataSource {

    @Throws
    suspend fun isFavorite(input: Video): Boolean

    @Throws
    suspend fun toggleFavorite(input: Video): Boolean

    @Throws
    suspend fun getFavorites(): List<Video>?

    @Throws
    suspend fun put(input: Video): Long

    @Throws
    suspend fun put(inputs: List<Video>): List<Long>?

    @Throws
    suspend fun putOfRegionCode(regionCode: String, inputs: List<Video>)

    @Throws
    suspend fun putOfEvent(eventType: String, inputs: List<Video>)

    @Throws
    suspend fun putIf(inputs: List<Video>): List<Long>?

    @Throws
    suspend fun isExists(id: String): Boolean

    @Throws
    suspend fun get(id: String): Video?

    @Throws
    suspend fun gets(): List<Video>?

    @Throws
    suspend fun gets(ids: List<String>): List<Video>?

    @Throws
    suspend fun gets(offset: Long, limit: Long): List<Video>?

    @Throws
    suspend fun getsOfQuery(query: String): List<Video>?

    @Throws
    suspend fun getsOfQuery(query: String, offset: Long, limit: Long): List<Video>?

    @Throws
    suspend fun getsOfCategoryId(categoryId: String): List<Video>?

    @Throws
    suspend fun getsOfCategoryId(categoryId: String, offset: Long, limit: Long): List<Video>?

    @Throws
    suspend fun getsOfRegionCode(regionCode: String, offset: Long, limit: Long): List<Video>?

    @Throws
    suspend fun getsOfEvent(eventType : String, offset: Long, limit: Long): List<Video>?

    @Throws
    suspend fun getsOfRelated(id: String, offset: Long, limit: Long): List<Video>?
}