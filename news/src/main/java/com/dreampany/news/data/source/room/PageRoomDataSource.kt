package com.dreampany.news.data.source.room

import com.dreampany.news.data.model.Page
import com.dreampany.news.data.source.api.PageDataSource
import com.dreampany.news.data.source.room.dao.PageDao


/**
 * Created by roman on 22/10/20
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