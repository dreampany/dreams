package com.dreampany.tools.data.source.crypto.room

import com.dreampany.common.data.enums.Order
import com.dreampany.tools.data.enums.crypto.CoinSort
import com.dreampany.tools.data.enums.crypto.Currency
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
class CoinRoomDataSource(
    private val mapper: CoinMapper,
    private val dao: CoinDao,
    private val quoteDao: QuoteDao
) : CoinDataSource {
    @Throws
    override suspend fun putItem(item: Coin): Long {
        mapper.add(item)
        if (item.hasQuote()) {
            quoteDao.insertOrReplace(item.getQuotesAsList())
        }
        return dao.insertOrReplace(item)
    }

    @Throws
    override suspend fun putItems(items: List<Coin>): List<Long>? {
        val result = arrayListOf<Long>()
        items.forEach { result.add(putItem(it)) }
        return result
    }

    @Throws
    override suspend fun getItems(): List<Coin>? = dao.items

    @Throws
    override suspend fun getItems(ids: List<String>, currency: Currency): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getItems(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        offset: Long,
        limit: Long
    ): List<Coin>? = mapper.getItems(currency, sort, order, offset, limit, quoteDao, this)

    @Throws
    override suspend fun getItem(id: String, currency: Currency): Coin? {
        TODO("Not yet implemented")
    }

}