package com.dreampany.lca.data.source.firebase.database

import androidx.core.util.Pair
import com.dreampany.firebase.RxFirebaseDatabase
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.framework.misc.exceptions.EmptyException
import com.dreampany.framework.util.DataUtil
import com.dreampany.lca.data.enums.CoinSource
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.model.Coin
import com.dreampany.lca.data.model.Quote
import com.dreampany.lca.data.source.api.CoinDataSource
import com.dreampany.lca.misc.Constants
import com.dreampany.network.manager.NetworkManager
import io.reactivex.Maybe
import java.util.ArrayList
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class DatabaseCoinDataSource constructor(
    val network: NetworkManager,
    val database: RxFirebaseDatabase
) : CoinDataSource {

    override fun isEmpty(source: CoinSource, currency: Currency, index: Int, limit: Int): Boolean {
        return false
    }

    override fun getRandomItem(source: CoinSource, currency: Currency): Coin? {
        return null
    }

    override fun getItems(
        source: CoinSource,
        currency: Currency,
        index: Int,
        limit: Int
    ): List<Coin>? {
        return null
    }

    override fun getItemsRx(
        source: CoinSource,
        currency: Currency,
        index: Int,
        limit: Int
    ): Maybe<List<Coin>> {
        return Maybe.empty()
    }

    override fun getItems(source: CoinSource, currency: Currency): List<Coin>? {
        return null
    }

    override fun getItemsRx(source: CoinSource, currency: Currency): Maybe<List<Coin>> {
        return Maybe.empty()
    }

    override fun getItems(source: CoinSource, currency: Currency, limit: Int): List<Coin>? {
        return null
    }

    override fun getItemsRx(source: CoinSource, currency: Currency, limit: Int): Maybe<List<Coin>> {
        return Maybe.empty()
    }

    override fun getItem(source: CoinSource, currency: Currency, id: String): Coin? {
        val path = Constants.FirebaseKey.CRYPTO + Constants.Sep.SLASH + Constants.FirebaseKey.COINS

        val quote = getQuote(currency, id)
        var coin: Coin? = null
        if (!DataUtil.isEmpty(quote)) {
            coin = database.getItemRx(path, quote.id, null, Coin::class.java).blockingGet()
        }
        return coin
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
        val path = Constants.FirebaseKey.CRYPTO + Constants.Sep.SLASH + Constants.FirebaseKey.COINS

        val quotes = getQuotes(currency, ids)
        var coins: MutableList<Coin>? = null
        if (!DataUtil.isEmpty(quotes)) {
            coins = ArrayList()
            for (quote in quotes) {
                val coin = database.getItemRx(path, quote.id, null, Coin::class.java).blockingGet()
                coins.add(coin)
            }
        }
        return coins
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
        return 0
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
        val path = Constants.FirebaseKey.CRYPTO + Constants.Sep.SLASH + Constants.FirebaseKey.COINS
        val child = coin.id

        val error = database.setItemRx(path, child, coin).blockingGet()
        if (error == null) {
            putQuote(coin)
            return 1
        }
        return -1
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

    override fun putItems(coins: List<Coin>): List<Long>? {
        for (coin in coins) {
            putItem(coin)
        }
        return ArrayList()
        /*        String path = Constants.FirebaseKey.CRYPTO.join(Constants.Sep.SLASH).join(Constants.FirebaseKey.COINS);
        Map<String, Coin> items = Maps.newHashMap();
        for (Coin coin : coins) {
            items.put(Constants.Sep.HYPHEN.join(String.valueOf(coin.getId())), coin);
        }
        Throwable error = database.setItemRx(path, items).blockingGet();
        if (error == null) {
            return new ArrayList<>();
        }
        return null;*/
    }

    override fun putItemsRx(coins: List<Coin>): Maybe<List<Long>> {
        return Maybe.create { emitter ->
            val result = putItems(coins)
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
        return null
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

    private fun putQuote(coin: Coin): Long {
        val latest = coin.getLatestQuote()
        return latest?.let { putQuote(it) } ?: 0
    }

    private fun putQuote(quote: Quote): Long {
        val path = Constants.FirebaseKey.CRYPTO + Constants.Sep.SLASH + Constants.FirebaseKey.QUOTES
        val child = quote.id + quote.currency.name

        val error = database.setItemRx(path, child, quote).blockingGet() ?: return 1
        return -1
    }

    private fun getQuote(currency: Currency, id: String): Quote {
        val path = Constants.FirebaseKey.CRYPTO + Constants.Sep.SLASH + Constants.FirebaseKey.QUOTES
        val coinDelayTime = TimeUtil.currentTime() - Constants.Time.Coin
        val greater = Pair.create(Constants.Quote.LAST_UPDATED, coinDelayTime.toString())
        val child = id + currency.name
        return database.getItemRx(path, child, greater, Quote::class.java).blockingGet()
    }

    private fun getQuotes(currency: Currency, ids: List<String>): List<Quote> {
        val path = Constants.FirebaseKey.CRYPTO + Constants.Sep.SLASH + Constants.FirebaseKey.QUOTES
        val coinDelayTime = TimeUtil.currentTime() - Constants.Time.Coin
        val greater = Pair.create(Constants.Quote.LAST_UPDATED, coinDelayTime.toString())
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