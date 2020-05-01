package com.dreampany.common.data.source.room

import com.dreampany.common.data.model.Store
import com.dreampany.common.data.source.api.StoreDataSource
import com.dreampany.common.data.source.mapper.StoreMapper
import com.dreampany.common.data.source.room.dao.StoreDao

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
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
    ): Boolean = dao.getCount(id, type, subtype, state) > 0

    @Throws
    override suspend fun putItem(item: Store): Long = dao.insertOrReplace(item)

    @Throws
    override suspend fun getItem(id: String, type: String, subtype: String, state: String): Store? =
        dao.getItem(id, type, subtype, state)

    override suspend fun getItems(type: String, subtype: String, state: String): List<String>? =
        dao.getItems(type, subtype, state)
}