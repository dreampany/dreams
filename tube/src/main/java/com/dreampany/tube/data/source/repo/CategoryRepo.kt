package com.dreampany.tube.data.source.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.source.api.CategoryDataSource
import com.dreampany.tube.data.source.mapper.CategoryMapper
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
class CategoryRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: AppPref,
    private val mapper: CategoryMapper,
    @Room private val room: CategoryDataSource,
    @Remote private val remote: CategoryDataSource
) : CategoryDataSource {

    @Throws
    override suspend fun isFavorite(input: Category) = withContext(Dispatchers.IO) {
        room.isFavorite(input)
    }

    @Throws
    override suspend fun toggleFavorite(input: Category) = withContext(Dispatchers.IO) {
        room.toggleFavorite(input)
    }

    override suspend fun getFavorites(): List<Category>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: Category): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Category>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): Category? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Category>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(regionCode: String) = withContext(Dispatchers.IO) {
        if (mapper.isExpired()) {
            val result = remote.gets(regionCode)
            if (!result.isNullOrEmpty()) {
                room.deleteAll()
                val result = room.put(result)
                if (!result.isNullOrEmpty()) {
                    mapper.commitExpire()
                }
            }
        }
        room.gets()
    }

    override suspend fun gets(ids: List<String>): List<Category>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(offset: Long, limit: Long): List<Category>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun deleteAll() = room.deleteAll()
}