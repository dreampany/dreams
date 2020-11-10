package com.dreampany.tools.data.source.crypto.repo

import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.tools.data.source.crypto.api.GraphDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 10/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class GraphRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Remote private val remote: GraphDataSource
) : GraphDataSource {

    @Throws
    override suspend fun read(slug: String, startTime: Long, endTime: Long)= withContext(Dispatchers.IO) {
      remote.read(slug, startTime, endTime)
    }
}