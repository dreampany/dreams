package com.dreampany.tube.data.source.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.data.source.api.CategoryDataSource
import com.dreampany.tube.data.source.mapper.CategoryMapper
import com.dreampany.tube.data.source.pref.Prefs
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
    private val pref: Prefs,
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

    override suspend fun get(id: String) = withContext(Dispatchers.IO) {
        room.get(id)
    }

    @Throws
    override suspend fun reads(): List<Category>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun reads(regionCode: String) = withContext(Dispatchers.IO) {
        if (mapper.isExpired) {
            val result = remote.reads(regionCode)
            if (!result.isNullOrEmpty()) {
                room.deleteAll()
                val result = room.put(result)
                if (!result.isNullOrEmpty()) {
                    mapper.commitExpire()
                }
            }
        }
        room.reads()
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