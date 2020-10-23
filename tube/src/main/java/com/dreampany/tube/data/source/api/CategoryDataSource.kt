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
    suspend fun readFavorites(): List<Category>?

    @Throws
    suspend fun write(input: Category): Long

    @Throws
    suspend fun write(inputs: List<Category>): List<Long>?

    @Throws
    suspend fun read(id: String): Category?

    @Throws
    suspend fun reads(): List<Category>?

    @Throws
    suspend fun reads(regionCode: String): List<Category>?

    @Throws
    suspend fun reads(ids: List<String>): List<Category>?

    @Throws
    suspend fun reads(offset: Long, limit: Long): List<Category>?

    @Throws
    suspend fun deleteAll()
}