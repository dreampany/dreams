package com.dreampany.crypto.data.source.api

import com.dreampany.crypto.data.model.Trade

/**
 * Created by roman on 2/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface TradeDataSource {
    @Throws
    suspend fun getTrades(fromSymbol: String, extraParams: String, limit: Long): List<Trade>?
}