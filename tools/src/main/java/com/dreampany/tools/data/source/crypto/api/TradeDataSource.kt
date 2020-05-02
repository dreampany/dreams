package com.dreampany.tools.data.source.crypto.api

import com.dreampany.tools.data.model.crypto.Trade

/**
 * Created by roman on 2/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface TradeDataSource {
    fun getTrades(fromSymbol: String, extraParams: String, limit: Long): List<Trade>?
}