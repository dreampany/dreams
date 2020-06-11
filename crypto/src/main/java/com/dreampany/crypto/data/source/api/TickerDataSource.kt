package com.dreampany.crypto.data.source.api

import com.dreampany.crypto.data.model.Ticker

/**
 * Created by roman on 11/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface TickerDataSource {
    @Throws
    suspend fun getTickers(id: String): List<Ticker>?
}