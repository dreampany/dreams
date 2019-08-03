package com.dreampany.lca.data.source.room

import com.annimon.stream.Stream
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.util.DataUtil
import com.dreampany.lca.data.enums.CoinSource
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.misc.CoinMapper
import com.dreampany.lca.data.model.Coin
import com.dreampany.lca.data.source.api.CoinDataSource
import com.dreampany.lca.data.source.dao.CoinDao
import com.dreampany.lca.data.source.dao.QuoteDao
import io.reactivex.Maybe
import java.util.*
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class RoomCoinDataSource constructor(
    val mapper: CoinMapper,
    val dao: CoinDao,
    val quoteDao: QuoteDao
) : CoinDataSource {

    @Volatile
    private var cacheLoaded: Boolean = false

    override fun isEmpty(source: CoinSource, currency: Currency, index: Int, limit: Int): Boolean {
        val count = getCount()
        return index + limit > count
    }

    override fun getRandomItem(source: CoinSource, currency: Currency): Coin {
        updateCache()
        return mapper.randomCoin
    }

    override fun getItems(
        source: CoinSource,
        currency: Currency,
        index: Int,
        limit: Int
    ): List<Coin>? {
        updateCache()
        val cache = mapper.sortedCoins
        if (DataUtil.isEmpty(cache)) {
            return null
        }
        val result = DataUtil.sub(cache, index, limit)
        if (DataUtil.isEmpty(result)) {
            return null
        }
        for (coin in result!!) {
            bindQuote(currency, coin)
        }
        return result
    }

    override fun getItemsRx(
        source: CoinSource,
        currency: Currency,
        index: Int,
        limit: Int
    ): Maybe<List<Coin>> {
        return Maybe.create { emitter ->
            val result = getItems(source, currency, index, limit)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun getItems(source: CoinSource, currency: Currency): List<Coin>? {
        updateCache()
        val cache = mapper.sortedCoins
        if (DataUtil.isEmpty(cache)) {
            return null
        }
        for (coin in cache) {
            bindQuote(currency, coin)
        }
        return cache
    }

    override fun getItemsRx(source: CoinSource, currency: Currency): Maybe<List<Coin>> {
        return Maybe.create { emitter ->
            val result = getItems(source, currency)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun getItems(source: CoinSource, currency: Currency, limit: Int): List<Coin>? {
        updateCache()
        val cache = mapper.sortedCoins
        if (DataUtil.isEmpty(cache)) {
            return null
        }
        val result = DataUtil.sub(cache, 0, limit)
        if (DataUtil.isEmpty(result)) {
            return null
        }
        for (coin in result) {
            bindQuote(currency, coin)
        }
        return result
    }

    override fun getItemsRx(source: CoinSource, currency: Currency, limit: Int): Maybe<List<Coin>> {
        return Maybe.create { emitter ->
            val result = getItems(source, currency, limit)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun getItem(source: CoinSource, currency: Currency, id: String): Coin? {
        if (!mapper.hasCoin(id)) {
            val room = dao.getItem(id)
            mapper.add(room)
        }
        val cache = mapper.getCoin(id)
        if (DataUtil.isEmpty(cache)) {
            return null
        }
        bindQuote(currency, cache)
        return cache
    }

    override fun getItemRx(source: CoinSource, currency: Currency, id: String): Maybe<Coin> {
        return Maybe.create { emitter ->
            val result = getItem(source, currency, id)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun getItems(source: CoinSource, currency: Currency, ids: List<String>): List<Coin>? {
        updateCache()
        val cache = mapper.getCoins(ids)
        if (DataUtil.isEmpty(cache)) {
            return null
        }
        Collections.sort(cache) { left, right -> left.rank - right.rank }
        for (coin in cache) {
            bindQuote(currency, coin)
        }
        return cache
    }

    override fun getItemsRx(
        source: CoinSource,
        currency: Currency,
        ids: List<String>
    ): Maybe<List<Coin>> {
        return Maybe.create { emitter ->
            val result = getItems(source, currency, ids)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        return Maybe.empty()
    }

    override fun getCount(): Int {
        return dao.count
    }

    override fun getCountRx(): Maybe<Int> {
        return Maybe.empty()
    }

    override fun isExists(coin: Coin): Boolean {
        return false
    }

    override fun isExistsRx(coin: Coin): Maybe<Boolean> {
        return Maybe.empty()
    }

    override fun putItem(coin: Coin): Long {
        mapper.add(coin) //adding mapper to reuse
        if (coin.hasQuote()) {
            quoteDao.insertOrReplace(coin.getQuotesAsList()!!)
        }
        return dao.insertOrReplace(coin)
    }

    override fun putItemRx(coin: Coin): Maybe<Long> {
        return Maybe.create { emitter ->
            val result = putItem(coin)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == -1L) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun putItems(coins: List<Coin>): List<Long> {
        val result = ArrayList<Long>()
        Stream.of(coins).forEach { coin -> result.add(putItem(coin)) }
        return result
    }

    override fun putItemsRx(coins: List<Coin>): Maybe<List<Long>> {
        return Maybe.create { emitter ->
            val result = putItems(coins)
            if (emitter.isDisposed) {
                return@create
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun delete(coin: Coin): Int {
        return 0
    }

    override fun deleteRx(coin: Coin): Maybe<Int> {
        return Maybe.empty()
    }

    override fun delete(coins: List<Coin>): List<Long>? {
        return null
    }

    override fun deleteRx(coins: List<Coin>): Maybe<List<Long>> {
        return Maybe.empty()
    }

    override fun getItem(id: String): Coin? {
        return dao.getItem(id)
    }

    override fun getItemRx(id: String): Maybe<Coin> {
        return Maybe.empty()
    }

    override fun getItems(): List<Coin>? {
        return null
    }

    override fun getItemsRx(): Maybe<List<Coin>> {
        return Maybe.empty()
    }

    override fun getItems(limit: Int): List<Coin>? {
        return null
    }

    override fun getItemsRx(limit: Int): Maybe<List<Coin>> {
        return Maybe.empty()
    }

    /* private */
    private fun updateCache() {
        if (!cacheLoaded || !mapper.hasCoins()) {
            val room = dao.items
            mapper.add(room)
            cacheLoaded = true
        }
    }

    private fun bindQuote(currency: Currency, coin: Coin?) {
        if (coin != null && !coin.hasQuote(currency)) {
            val quote = quoteDao.getItems(coin.id, currency.name)
            coin.addQuote(quote)
        }
    }
}