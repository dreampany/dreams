package com.dreampany.tools.data.source.crypto.api

import com.dreampany.tools.data.model.crypto.Graph

/**
 * Created by roman on 10/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface GraphDataSource {
    @Throws
    suspend fun read(slug: String, startTime: Long, endTime: Long): Graph?
}