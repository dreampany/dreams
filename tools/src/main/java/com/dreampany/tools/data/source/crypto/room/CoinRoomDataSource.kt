package com.dreampany.tools.data.source.crypto.room

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
            mapper.writeFavorite(input)
        }
        return favorite.not()
    }

    @Throws
    override suspend fun readsFavorite(
        currency: Currency,
        sort: String,
        order: String
    ): List<Coin>? = mapper.getFavoriteItems(currency, sort, order, quoteDao, this)

    @Throws
    override suspend fun write(input: Coin): Long {
        mapper.add(input)
        if (input.hasQuote()) {
            quoteDao.insertOrReplace(input.getQuotesAsList())
        }
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun write(inputs: List<Coin>): List<Long>? {
        val result = arrayListOf<Long>()
        inputs.forEach { result.add(write(it)) }
        return result
    }

    @Throws
    override suspend fun read(currency: Currency, id: String): Coin? =
        mapper.getItem(currency, id, quoteDao, this)

    @Throws
    override suspend fun reads(): List<Coin>? = dao.items

    @Throws
    override suspend fun reads(currency: Currency, ids: List<String>): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun reads(
        currency: Currency,
        sort: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Coin>? = mapper.getItems(currency, sort, order, offset, limit, quoteDao, this)
}