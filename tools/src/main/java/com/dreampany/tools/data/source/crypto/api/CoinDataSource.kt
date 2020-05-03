package com.dreampany.tools.data.source.crypto.api

import com.dreampany.framework.data.enums.Order
import com.dreampany.tools.data.enums.crypto.CoinSort
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.data.model.crypto.Coin

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CoinDataSource {

    @Throws
    suspend fun isFavorite(input: Coin): Boolean

    @Throws
    suspend fun toggleFavorite(input: Coin): Boolean

    @Throws
    suspend fun putItem(input: Coin): Long

    @Throws
    suspend fun insert(inputs: List<Coin>): List<Long>?

    @Throws
    suspend fun getCoins(): List<Coin>?

    @Throws
    suspend fun getCoin(id: String, currency: Currency): Coin?

    @Throws
    suspend fun getCoins(ids: List<String>, currency: Currency): List<Coin>?

    @Throws
    suspend fun getCoins(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        offset: Long,
        limit: Long
    ): List<Coin>?

    @Throws
    suspend fun getFavoriteCoins(
        currency: Currency,
        sort: CoinSort,
        order: Order
    ): List<Coin>?
}