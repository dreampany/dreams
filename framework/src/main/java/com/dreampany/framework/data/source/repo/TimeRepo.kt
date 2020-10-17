package com.dreampany.framework.data.source.repo

import com.dreampany.framework.data.model.Time
import com.dreampany.framework.data.source.api.TimeDataSource
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 28/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class TimeRepo
@Inject constructor(
    rx: RxMapper,
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