package com.dreampany.nearby.data.source.api

import com.dreampany.nearby.data.model.media.Apk

/**
 * Created by roman on 28/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ApkDataSource {

    @Throws
    suspend fun isFavorite(input: Apk): Boolean

    @Throws
    suspend fun toggleFavorite(input: Apk): Boolean

    @Throws
    suspend fun getFavorites(): List<Apk>?

    @Throws
    suspend fun put(input: Apk): Long

    @Throws
    suspend fun put(inputs: List<Apk>): List<Long>?

    @Throws
    suspend fun get(id: String): Apk?

    @Throws
    suspend fun gets(): List<Apk>?

    @Throws
    suspend fun gets(ids: List<String>): List<Apk>?

    @Throws
    suspend fun gets(offset: Long, limit: Long): List<Apk>?
}