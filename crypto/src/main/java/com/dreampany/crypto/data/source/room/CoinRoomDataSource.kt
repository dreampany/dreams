package com.dreampany.crypto.data.source.room

import com.dreampany.framework.data.enums.Order
import com.dreampany.crypto.data.enums.Sort
import com.dreampany.crypto.data.enums.Currency
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.data.source.api.CoinDataSource
import com.dreampany.crypto.data.source.mapper.CoinMapper
import com.dreampany.crypto.data.source.room.dao.CoinDao
import com.dreampany.crypto.data.source.room.dao.QuoteDao

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
    override suspend fun getFavorites(
        currency: Currency,
        sort: Sort,
        order: Order
    ): List<Coin>? = mapper.getFavoriteItems(currency, sort, order, quoteDao, this)

    @Throws
    override suspend fun put(input: Coin): Long {
        mapper.add(input)
        if (input.hasQuote()) {
            quoteDao.insertOrReplace(input.getQuotesAsList())
        }
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun put(inputs: List<Coin>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(put(it)) }
        return result
    }

    @Throws
    override suspend fun get(id: String, currency: Currency): Coin? =
        mapper.getItem(id, currency, quoteDao, this)

    @Throws
    override suspend fun gets(): List<Coin>? = dao.items

    @Throws
    override suspend fun gets(ids: List<String>, currency: Currency): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(
        currency: Currency,
        sort: Sort,
        order: Order,
        offset: Long,
        limit: Long
    ): List<Coin>? = mapper.getItems(currency, sort, order, offset, limit, quoteDao, this)
}