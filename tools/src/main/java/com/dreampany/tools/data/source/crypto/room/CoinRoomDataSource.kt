package com.dreampany.tools.data.source.crypto.room

import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.mapper.CoinMapper
import com.dreampany.tools.data.source.crypto.mapper.QuoteMapper
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
    private val quoteMapper: QuoteMapper,
    private val dao: CoinDao,
    private val quoteDao: QuoteDao
) : CoinDataSource {

    @Throws
    @Synchronized
    override suspend fun isFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Coin): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun favorites(currency: Currency, sort: String, order: String): List<Coin>? {
        TODO("Not yet implemented")
    }

    @Throws
    @Synchronized
    override suspend fun write(input: Pair<Coin, Quote>): Long {
        mapper.write(input.first)
        quoteMapper.write(input.second)

        val coinResult = dao.insertOrReplace(input.first)
        quoteDao.insertOrReplace(input.second)
        return coinResult
    }

    @Throws
    @Synchronized
    override suspend fun write(inputs: List<Pair<Coin, Quote>>): List<Long>? =
        inputs.map { write(it) }

    @Throws
    @Synchronized
    override suspend fun read(id: String, currency: Currency): Pair<Coin, Quote>? {
        val coin = mapper.read(id, currency, dao) ?: return null
        val quote = quoteMapper.read(id, currency, quoteDao) ?: return null
        return Pair(coin, quote)
    }

    override suspend fun reads(): List<Pair<Coin, Quote>>? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(ids: List<String>, currency: Currency): List<Pair<Coin, Quote>>? {
        TODO("Not yet implemented")
    }

    @Throws
    @Synchronized
    override suspend fun reads(
        currency: Currency,
        sort: String,
        order: String,
        offset: Long,
        limit: Long
    ): List<Pair<Coin, Quote>>? {
        val coins = mapper.reads(currency, sort, order, offset, limit, dao)
        return coins?.mapNotNull {
            val quote = quoteMapper.read(it.id, currency, quoteDao)
            if (quote == null) null else Pair(it, quote)
        }
    }

/* @Throws
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
*//*        if (input.hasQuote()) {
            quoteDao.insertOrReplace(input.getQuotesAsList())
        }*//*
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
    ): List<Coin>? = mapper.read(currency, sort, order, offset, limit, quoteDao, this)*/
}