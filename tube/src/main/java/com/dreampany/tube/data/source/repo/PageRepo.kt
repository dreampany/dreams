package com.dreampany.tube.data.source.repo

import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tube.data.model.Page
import com.dreampany.tube.data.source.api.PageDataSource
import com.dreampany.tube.data.source.pref.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class PageRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: Prefs,
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