package com.dreampany.common.data.source.repo

import com.dreampany.common.data.model.Time
import com.dreampany.common.data.source.api.TimeDataSource
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
class TimeRepo
@Inject constructor(
    rm: ResponseMapper,
    @Room private val room: TimeDataSource
) : TimeDataSource {

    @Throws
    override suspend fun write(item: Time) = withContext(Dispatchers.IO) {
        room.write(item)
    }

    @Throws
    override suspend fun read(id: String, type: String, subtype: String, state: String)=
        withContext(Dispatchers.IO) {
            room.read(id, type, subtype, state)
        }

    @Throws
    override suspend fun readTime(id: String, type: String, subtype: String, state: String) =
        withContext(Dispatchers.IO) {
            room.readTime(id, type, subtype, state)
        }
}