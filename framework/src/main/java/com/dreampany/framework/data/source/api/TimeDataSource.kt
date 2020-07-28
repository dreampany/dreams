package com.dreampany.framework.data.source.api

import com.dreampany.framework.data.model.Time

/**
 * Created by roman on 28/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface TimeDataSource {

    @Throws
    suspend fun insert(item: Time): Long

    @Throws
    suspend fun getTime(id: String, type: String, subtype: String, state: String): Long
}