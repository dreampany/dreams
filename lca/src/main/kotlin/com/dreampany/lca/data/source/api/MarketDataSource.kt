package com.dreampany.lca.data.source.api

import com.dreampany.frame.data.source.api.DataSource
import com.dreampany.lca.data.model.Market
import io.reactivex.Maybe

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface MarketDataSource : DataSource<Market> {
    fun getItems(fromSymbol: String, toSymbol: String, limit: Int): List<Market>?

    fun getItemsRx(fromSymbol: String, toSymbol: String, limit: Int): Maybe<List<Market>>
}