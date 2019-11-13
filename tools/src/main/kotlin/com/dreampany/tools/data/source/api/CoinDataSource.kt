package com.dreampany.tools.data.source.api

import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.Coin
import io.reactivex.Maybe

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CoinDataSource : DataSource<Coin> {

    fun getItems(
        currency: Currency,
        sort: String,
        sortDirection: String,
        auxiliaries: String,
        start: Long,
        limit: Long
    ): List<Coin>?

    fun getItemsRx(
        currency: Currency,
        sort: String,
        sortDirection: String,
        auxiliaries: String,
        start: Long,
        limit: Long
    ): Maybe<List<Coin>>
}