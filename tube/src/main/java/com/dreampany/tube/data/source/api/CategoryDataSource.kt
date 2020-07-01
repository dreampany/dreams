package com.dreampany.tube.data.source.api

import com.dreampany.tube.data.model.Category

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CategoryDataSource {
    @Throws
    suspend fun isFavorite(input: Category): Boolean

    @Throws
    suspend fun toggleFavorite(input: Category): Boolean

    @Throws
    suspend fun getFavorites(): List<Category>?

    @Throws
    suspend fun put(input: Category): Long

    @Throws
    suspend fun put(inputs: List<Category>): List<Long>?

    @Throws
    suspend fun get(id: String): Category?

    @Throws
    suspend fun gets(): List<Category>?

    @Throws
    suspend fun gets(regionCode: String): List<Category>?

    @Throws
    suspend fun gets(ids: List<String>): List<Category>?

    @Throws
    suspend fun gets(offset: Long, limit: Long): List<Category>?
}