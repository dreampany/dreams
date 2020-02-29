package com.dreampany.tools.data.source.database

import androidx.core.util.Pair
import com.dreampany.firebase.RxFirebaseDatabase
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.data.mapper.crypto.CoinMapper
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.api.crypto.CoinDataSource
import com.dreampany.tools.misc.Constants
import io.reactivex.Maybe
import java.util.ArrayList

/**
 * Created by roman on 2/27/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DatabaseCoinDataSource
constructor(
    private val network: NetworkManager,
    private val mapper: CoinMapper,
    private val database: RxFirebaseDatabase
) : CoinDataSource {
    override fun getItem(currency: Currency, id: String): Coin? {
        var coin: Coin? = null
        val quote = getQuote(currency, id)
        val path = Constants.Keys.Coin.CRYPTO + Constants.Sep.SLASH + Constants.Keys.Coin.COINS
        coin = database.getItemRx(path, quote.id, null, Coin::class.java).blockingGet()
        return coin
    }

    override fun getItem(id: String): Coin? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(currency: Currency, id: String): Maybe<Coin> {
        return Maybe.create { emitter ->
            val result = getItem(currency, id)
            if (emitter.isDisposed) return@create
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun getItemRx(id: String): Maybe<Coin> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun getItems(currency: Currency, ids: List<String>): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long,
        limit: Long
    ): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(currency: Currency, ids: List<String>): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        val path = Constants.Keys.Coin.CRYPTO + Constants.Sep.SLASH + Constants.Keys.Coin.COINS
        val child = t.id

        val error = database.setItemRx(path, child, t).blockingGet()
        if (error == null) {
            putQuote(t)
            return 1
        }
        return -1
    }

    override fun putItemRx(t: Coin): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Coin>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Coin>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: Coin): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Coin>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Coin): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Coin>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun putQuote(coin: Coin): Long {
        val latest = coin.getLatestQuote()
        return latest?.let { putQuote(it) } ?: 0
    }

    private fun putQuote(quote: Quote): Long {
        val path = Constants.Keys.Coin.CRYPTO + Constants.Sep.SLASH + Constants.Keys.Coin.QUOTES
        val child = quote.id + quote.currency.name

        val error = database.setItemRx(path, child, quote).blockingGet() ?: return 1
        return -1
    }

    private fun getQuote(currency: Currency, id: String): Quote {
        val path = Constants.Keys.Coin.CRYPTO + Constants.Sep.SLASH + Constants.Keys.Coin.QUOTES
        val coinDelayTime = TimeUtilKt.currentMillis() - Constants.Time.Crypto.COIN
        val greater = Pair.create(Constants.Keys.Quote.LAST_UPDATED, coinDelayTime.toString())
        val child = id + currency.name
        return database.getItemRx(path, child, greater, Quote::class.java).blockingGet()
    }

    private fun getQuotes(currency: Currency, ids: List<String>): List<Quote> {
        val path = Constants.Keys.Coin.CRYPTO + Constants.Sep.SLASH + Constants.Keys.Coin.QUOTES
        val coinDelayTime = TimeUtilKt.currentMillis() - Constants.Time.Crypto.COIN
        val greater = Pair.create(Constants.Keys.Quote.LAST_UPDATED, coinDelayTime.toString())
        val quotes = ArrayList<Quote>()
        for (id in ids) {
            val child = id + currency.name
            val quote = database.getItemRx(path, child, greater, Quote::class.java).blockingGet()
            if (quote != null) {
                quotes.add(quote)
            }
        }

        return quotes
    }
}