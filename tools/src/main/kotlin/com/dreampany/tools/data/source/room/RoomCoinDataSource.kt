package com.dreampany.tools.data.source.room

import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.data.mapper.CoinMapper
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.source.api.CoinDataSource
import com.dreampany.tools.data.source.room.dao.CoinDao
import com.dreampany.tools.data.source.room.dao.QuoteDao
import io.reactivex.Maybe

/**
 * Created by roman on 2019-11-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RoomCoinDataSource(
    private val mapper: CoinMapper,
    private val dao: CoinDao,
    private val quoteDao: QuoteDao
) : CoinDataSource {

    override fun getItem(currency: Currency, id: String): Coin? {
        if (!mapper.hasCoin(id)) {
            val room = dao.getItem(id)
            mapper.add(room)
        }
        val cache = mapper.get(id)
        if (cache == null) return null
        bindQuote(currency, cache)
        return cache
    }

    override fun getItems(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long,
        limit: Long
    ): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long,
        limit: Long
    ): Maybe<List<Coin>> {
        return mapper.getItemsRx(currency, sort, order, start, limit, quoteDao, this)
    }

    override fun getItems(currency: Currency, ids: List<String>): List<Coin>? {
        return mapper.getItems(currency, ids, quoteDao, this)
    }

    override fun getItemsRx(currency: Currency, ids: List<String>): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Coin>? {
        return dao.items
    }

    override fun getItems(limit: Long): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return dao.count
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: Coin): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Coin): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Coin): Long {
        mapper.add(t) //adding mapper to reuse
        if (t.hasQuote()) {
            quoteDao.insertOrReplace(t.getQuotesAsList()!!)
        }
        return dao.insertOrReplace(t)
    }

    override fun putItemRx(t: Coin): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Coin>): List<Long>? {
        val result = arrayListOf<Long>()
        ts.forEach { coin -> result.add(putItem(coin)) }
        return result
    }

    override fun putItemsRx(ts: List<Coin>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: Coin): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Coin): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Coin>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Coin>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Coin? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(currency: Currency, id: String): Maybe<Coin> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Coin> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun bindQuote(currency: Currency, coin: Coin?) {
        if (coin != null && !coin.hasQuote(currency)) {
            val quote = quoteDao.getItem(coin.id, currency.name)
            if (quote != null)
                coin.addQuote(quote)
        }
    }
}