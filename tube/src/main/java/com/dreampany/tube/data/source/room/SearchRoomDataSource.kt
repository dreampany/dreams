package com.dreampany.tube.data.source.room

import com.dreampany.tube.data.model.Search
import com.dreampany.tube.data.source.api.SearchDataSource
import com.dreampany.tube.data.source.room.dao.SearchDao

/**
 * Created by roman on 18/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SearchRoomDataSource(
    private val dao: SearchDao
) : SearchDataSource {

    @Throws
    override suspend fun write(input: Search): Long =
        dao.insertOrReplace(input)

    @Throws
    override suspend fun hit(id: String, ref: String): Long {
        TODO("Not yet implemented")
    }
}