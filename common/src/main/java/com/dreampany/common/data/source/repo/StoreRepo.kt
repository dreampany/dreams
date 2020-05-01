package com.dreampany.common.data.source.repo

import com.dreampany.common.data.model.Store
import com.dreampany.common.data.source.api.StoreDataSource
import com.dreampany.common.data.source.mapper.StoreMapper
import com.dreampany.common.inject.annote.Room
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
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
    override suspend fun isExists(
        id: String,
        type: String,
        subtype: String,
        state: String
    )= withContext(Dispatchers.IO) {
        room.isExists(id, type, subtype, state)
    }

    override suspend fun putItem(item: Store)= withContext(Dispatchers.IO) {
        room.putItem(item)
    }

    override suspend fun getItem(id: String, type: String, subtype: String, state: String)= withContext(Dispatchers.IO) {
        room.getItem(id, type, subtype, state)
    }
}