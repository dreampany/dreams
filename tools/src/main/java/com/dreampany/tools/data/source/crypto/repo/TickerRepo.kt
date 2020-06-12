package com.dreampany.tools.data.source.crypto.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tools.data.source.crypto.api.TickerDataSource
import com.dreampany.tools.data.source.crypto.mapper.TickerMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 11/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class TickerRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val mapper: TickerMapper,
    @Remote private val remote: TickerDataSource
) : TickerDataSource {
    @Throws
    override suspend fun getTickers(id: String) = withContext(Dispatchers.IO) {
        remote.getTickers(id)
    }
}