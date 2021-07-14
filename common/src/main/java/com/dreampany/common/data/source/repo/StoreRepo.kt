package com.dreampany.common.data.source.repo

import com.dreampany.common.data.model.Store
import com.dreampany.common.data.source.api.StoreDataSource
import com.dreampany.common.data.source.mapper.StoreMapper
import com.dreampany.common.inject.qualifier.Room
import com.dreampany.common.misc.func.ResponseMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class StoreRepo
@Inject constructor(
    rm: ResponseMapper,
    private val mapper: StoreMapper,
    @Room private val room: StoreDataSource
) : StoreDataSource {

    @Throws
    override suspend fun isExists(
        id: String,
        type: String,
        subtype: String,
        state: String
    ) = withContext(Dispatchers.IO) {
        room.isExists(id, type, subtype, state)
    }

    @Throws
    override suspend fun write(input: Store) = withContext(Dispatchers.IO) {
        room.write(input)
    }

    @Throws
    override suspend fun read(id: String, type: String, subtype: String, state: String) =
        withContext(Dispatchers.IO) {
            room.read(id, type, subtype, state)
        }

    @Throws
    override suspend fun reads(type: String, subtype: String, state: String) =
        withContext(Dispatchers.IO) {
            room.reads(type, subtype, state)
        }

    @Throws
    override suspend fun delete(store: Store) =
        withContext(Dispatchers.IO) {
            room.delete(store)
        }
}