package com.dreampany.tools.data.source.radio.repo

import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tools.data.model.radio.Page
import com.dreampany.tools.data.source.radio.api.PageDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 8/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class PageRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Room private val room: PageDataSource
) : PageDataSource {

    @Throws
    override suspend fun write(input: Page) = withContext(Dispatchers.IO) {
        room.write(input)
    }

    @Throws
    override suspend fun write(inputs: List<Page>) = withContext(Dispatchers.IO) {
        room.write(inputs)
    }

    @Throws
    override suspend fun read(id: String) = withContext(Dispatchers.IO) {
        room.read(id)
    }

    @Throws
    override suspend fun reads() = withContext(Dispatchers.IO) {
        room.reads()
    }
}