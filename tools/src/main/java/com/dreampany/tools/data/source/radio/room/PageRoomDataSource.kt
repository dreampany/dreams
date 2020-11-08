package com.dreampany.tools.data.source.radio.room

import com.dreampany.tools.data.model.radio.Page
import com.dreampany.tools.data.source.radio.api.PageDataSource
import com.dreampany.tools.data.source.radio.room.dao.PageDao

/**
 * Created by roman on 8/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PageRoomDataSource(
    private val dao: PageDao
) : PageDataSource {

    @Throws
    override suspend fun write(input: Page): Long =
        dao.insertOrReplace(input)

    @Throws
    override suspend fun write(inputs: List<Page>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(write(it)) }
        return result
    }

    @Throws
    override suspend fun read(id: String): Page? = dao.read(id)

    @Throws
    override suspend fun reads(): List<Page>? = dao.all
}