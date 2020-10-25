package com.dreampany.framework.data.source.repo

import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.source.api.StoreDataSource
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StoreRepo
@Inject constructor(
    rx: RxMapper,
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