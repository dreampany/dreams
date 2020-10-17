package com.dreampany.framework.data.source.room

import com.dreampany.framework.data.model.Time
import com.dreampany.framework.data.source.api.TimeDataSource
import com.dreampany.framework.data.source.room.dao.TimeDao

/**
 * Created by roman on 28/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
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