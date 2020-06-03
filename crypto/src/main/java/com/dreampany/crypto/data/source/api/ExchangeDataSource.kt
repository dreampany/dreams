package com.dreampany.crypto.data.source.api

import com.dreampany.crypto.data.model.Exchange

/**
 * Created by roman on 2/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ExchangeDataSource {
    suspend fun getExchanges(
        fromSymbol: String,
        toSymbol: String,
        extraParams: String,
        limit: Long
    ): List<Exchange>?
}