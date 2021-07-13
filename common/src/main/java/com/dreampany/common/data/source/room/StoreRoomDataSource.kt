package com.dreampany.common.data.source.room

import com.dreampany.common.data.model.Store
import com.dreampany.common.data.source.api.StoreDataSource
import com.dreampany.common.data.source.mapper.StoreMapper
import com.dreampany.common.data.source.room.dao.StoreDao

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class StoreRoomDataSource
constructor(
    private val mapper: StoreMapper,
    private val dao: StoreDao
) : StoreDataSource {

    @Throws
    override suspend fun isExists(
        id: String,
        type: String,
        subtype: String,
        state: String
    ): Boolean = dao.readCount(id, type, subtype, state) > 0

    @Throws
    override suspend fun write(input: Store): Long = dao.insertOrReplace(input)

    @Throws
    override suspend fun read(id: String, type: String, subtype: String, state: String): Store? =
        dao.read(id, type, subtype, state)

    @Throws
    override suspend fun reads(type: String, subtype: String, state: String): List<Store>? =
        dao.reads(type, subtype, state)

    @Throws
    override suspend fun delete(store: Store): Int = dao.delete(store)

}