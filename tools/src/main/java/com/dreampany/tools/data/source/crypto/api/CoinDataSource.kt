package com.dreampany.tools.data.source.crypto.api

import com.dreampany.common.data.enums.Order
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CoinDataSource {

    @Throws
    suspend fun getCoins(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long,
        limit: Long
    ): List<Coin>?

    @Throws
    suspend fun getCoins(currency: Currency, ids: List<String>): List<Coin>?

    @Throws
    suspend fun getCoin(currency: Currency, id: String): Coin?
}