package com.dreampany.tools.data.source.api.crypto

import com.dreampany.tools.data.model.crypto.Trade
import io.reactivex.Maybe

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface TradeDataSource {
    fun getTrades(extraParams: String, fromSymbol: String, limit: Long): List<Trade>?
    fun getTradesRx(extraParams: String, fromSymbol: String, limit: Long): Maybe<List<Trade>>
}