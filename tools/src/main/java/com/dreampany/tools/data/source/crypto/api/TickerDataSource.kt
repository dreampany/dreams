package com.dreampany.tools.data.source.crypto.api

import com.dreampany.tools.data.model.crypto.Ticker

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