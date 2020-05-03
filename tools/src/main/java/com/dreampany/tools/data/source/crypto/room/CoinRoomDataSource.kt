package com.dreampany.tools.data.source.crypto.room

import com.dreampany.framework.data.enums.Order
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
    override suspend fun isFavorite(input: Coin): Boolean = mapper.isFavorite(input)

    @Throws
    override suspend fun toggleFavorite(input: Coin): Boolean {
        val favorite = isFavorite(input)
        if (favorite) {
            mapper.deleteFavorite(input)
        } else {
            mapper.insertFavorite(input)
        }
        return favorite.not()
    }

    @Throws
    override suspend fun putItem(input: Coin): Long {
        mapper.add(input)
        if (input.hasQuote()) {
            quoteDao.insertOrReplace(input.getQuotesAsList())
        }
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun insert(inputs: List<Coin>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(putItem(it)) }
        return result
    }

    @Throws
    override suspend fun getCoins(): List<Coin>? = dao.items

    @Throws
    override suspend fun getCoins(ids: List<String>, currency: Currency): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun getCoins(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        offset: Long,
        limit: Long
    ): List<Coin>? = mapper.getItems(currency, sort, order, offset, limit, quoteDao, this)

    @Throws
    override suspend fun getCoin(id: String, currency: Currency): Coin? =
        mapper.getItem(id, currency, quoteDao, this)

    @Throws
    override suspend fun getFavoriteCoins(
        currency: Currency,
        sort: CoinSort,
        order: Order
    ): List<Coin>? = mapper.getFavoriteItems(currency, sort, order, quoteDao, this)
}