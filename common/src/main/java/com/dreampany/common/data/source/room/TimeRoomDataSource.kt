package com.dreampany.common.data.source.room

import com.dreampany.common.data.model.Time
import com.dreampany.common.data.source.api.TimeDataSource
import com.dreampany.common.data.source.room.dao.TimeDao

/**
 * Created by roman on 7/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class TimeRoomDataSource
constructor(
    private val dao: TimeDao
) : TimeDataSource {

    @Throws
    override suspend fun write(item: Time): Long = dao.insertOrReplace(item)

    @Throws
    override suspend fun read(id: String, type: String, subtype: String, state: String) : Time?
            = dao.read(id, type, subtype, state)

    @Throws
    override suspend fun readTime(id: String, type: String, subtype: String, state: String): Long =
        dao.readTime(id, type, subtype, state)
}