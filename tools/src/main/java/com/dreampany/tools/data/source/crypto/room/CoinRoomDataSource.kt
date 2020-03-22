package com.dreampany.tools.data.source.crypto.room

import com.dreampany.common.data.enums.Order
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
import com.dreampany.tools.data.source.crypto.room.dao.CoinDao
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinRoomDataSource
constructor(
    private val mapper: CoinMapper,
    private val dao: CoinDao,
    private val quoteDao: QuoteDao
) : CoinDataSource {
    override suspend fun putCoins(coins: List<Coin>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun getCoins(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long,
        limit: Long
    ): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun getCoins(currency: Currency, ids: List<String>): List<Coin>? {
        TODO("Not yet implemented")
    }

    override suspend fun getCoin(currency: Currency, id: String): Coin? {
        TODO("Not yet implemented")
    }
}