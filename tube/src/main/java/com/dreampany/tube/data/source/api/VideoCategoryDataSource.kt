package com.dreampany.tube.data.source.api

import com.dreampany.tube.data.model.Video

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface VideoCategoryDataSource {
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
    suspend fun get(id: String): Video?

    @Throws
    suspend fun gets(): List<Video>?

    @Throws
    suspend fun gets(query: String): List<Video>?

    @Throws
    suspend fun gets(ids: List<String>): List<Video>?

    @Throws
    suspend fun gets(offset: Long, limit: Long): List<Video>?
}