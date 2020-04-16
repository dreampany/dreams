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
    suspend fun putItem(item: Coin): Long

    @Throws
    suspend fun putItems(items: List<Coin>): List<Long>?

    @Throws
    suspend fun getItems(): List<Coin>?

    @Throws
    suspend fun getItem(id: String, currency: Currency): Coin?

    @Throws
    suspend fun getItems(ids: List<String>, currency: Currency): List<Coin>?

    @Throws
    suspend fun getItems(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        offset: Long,
        limit: Long
    ): List<Coin>
}