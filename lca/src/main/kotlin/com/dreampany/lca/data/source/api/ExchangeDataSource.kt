package com.dreampany.lca.data.source.api

import com.dreampany.frame.data.source.api.DataSource
import com.dreampany.lca.data.model.Exchange
import io.reactivex.Maybe

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ExchangeDataSource : DataSource<Exchange> {
    fun getItems(symbol: String, limit: Int): List<Exchange>?

    fun getItemsRx(symbol: String, limit: Int): Maybe<List<Exchange>>
}