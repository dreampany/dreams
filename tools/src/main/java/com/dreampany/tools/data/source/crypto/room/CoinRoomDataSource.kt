package com.dreampany.tools.data.source.crypto.room

import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
import com.dreampany.tools.data.source.crypto.room.dao.CoinDao

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinRoomDataSource(
    private val mapper: CoinMapper,
    private val dao: CoinDao
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
    override suspend fun favorites(
        currency: Currency,
        sort: String,
        order: String
    ): List<Coin>? = mapper.readFavorites(currency, sort, order, quoteDao, this)

    @Throws
    override suspend fun write(input: Coin): Long {
        mapper.write(input)
/*        if (input.hasQuote()) {
            quoteDao.insertOrReplace(input.getQuotesAsList())
        }*/
        return dao.insertOrReplace(input)
    }

    @Throws
    override suspend fun write(inputs: List<Coin>): List<Long>? = inputs.map { write(it) }

    @Throws
    override suspend fun read(currency: Currency, id: String): Coin? =
        mapper.read(currency, id, quoteDao, this)

    @Throws
    override suspend fun reads(): List<Coin>? = dao.all

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
    ): List<Coin>? = mapper.read(currency, sort, order, offset, limit, quoteDao, this)
}