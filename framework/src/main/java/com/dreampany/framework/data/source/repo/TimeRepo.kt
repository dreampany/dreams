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
    override suspend fun insert(item: Time) = withContext(Dispatchers.IO) {
        room.insert(item)
    }

    @Throws
    override suspend fun getTime(id: String, type: String, subtype: String, state: String) =
        withContext(Dispatchers.IO) {
            room.getTime(id, type, subtype, state)
        }
}