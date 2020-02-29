package com.dreampany.tools.data.source.api.crypto

import com.dreampany.tools.data.model.crypto.Exchange
import io.reactivex.Maybe

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ExchangeDataSource {
    fun getExchanges(extraParams: String, fromSymbol: String, toSymbol: String, limit: Long): List<Exchange>?
    fun getExchangesRx(extraParams: String, fromSymbol: String, toSymbol: String, limit: Long): Maybe<List<Exchange>>
}