package com.dreampany.lca.data.source.firebase.firestore

import com.dreampany.firebase.RxFirebaseFirestore
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.lca.data.enums.CoinSource
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.model.Coin
import com.dreampany.lca.data.model.Quote
import com.dreampany.lca.data.source.api.CoinDataSource
import com.dreampany.lca.misc.Constants
import com.dreampany.network.manager.NetworkManager
import com.google.common.collect.Maps
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import org.apache.commons.lang3.tuple.MutablePair
import org.apache.commons.lang3.tuple.MutableTriple
import timber.log.Timber
import java.util.*
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class FirestoreCoinDataSource constructor(
    val network: NetworkManager,
    val firestore: RxFirebaseFirestore
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
        return null
    }


    override fun getItemRx(source: CoinSource, currency: Currency, id: String): Maybe<Coin> {
        val collection = Constants.FirebaseKey.CRYPTO
        val paths = TreeSet<MutablePair<String, String>>()
        paths.add(MutablePair.of(source.name, Constants.FirebaseKey.COINS))

        val lastUpdated = TimeUtil.currentTime() - Constants.Time.Coin

        val equalTo = ArrayList<MutablePair<String, Any>>()
        val greaterThanOrEqualTo = ArrayList<MutablePair<String, Any>>()

        equalTo.add(MutablePair.of(Constants.Coin.ID, id))


        greaterThanOrEqualTo.add(MutablePair.of(Constants.Coin.LAST_UPDATED, lastUpdated))

        var result = firestore.getItemRx(
            collection, paths, equalTo, null, greaterThanOrEqualTo,
            Coin::class.java
        )

        result = result.doOnSuccess { }

        return result
    }

    override fun getItems(source: CoinSource, currency: Currency, ids: List<String>): List<Coin>? {

        val quotes = getQuotes(source, currency, ids)

        if (DataUtil.isEmpty(quotes)) {
            return null
        }

        val collection = Constants.FirebaseKey.CRYPTO

        val paths = TreeSet<MutablePair<String, String>>()
        paths.add(MutablePair.of(source.name, Constants.FirebaseKey.COINS))

        val lastUpdated = TimeUtil.currentTime() - Constants.Time.Coin

        val equalTo = ArrayList<MutablePair<String, Any>>()
        val greaterThanOrEqualTo = ArrayList<MutablePair<String, Any>>()

        for (quote in quotes) {
            equalTo.add(MutablePair.of(Constants.Coin.ID, quote.id))
        }

        greaterThanOrEqualTo.add(MutablePair.of(Constants.Coin.LAST_UPDATED, lastUpdated))

        return firestore.getItemsRx(
            collection, paths, equalTo, null, greaterThanOrEqualTo,
            Coin::class.java
        ).blockingGet()
    }

    override fun getItemsRx(
        source: CoinSource,
        currency: Currency,
        ids: List<String>
    ): Maybe<List<Coin>> {

        return Maybe.create { emitter ->
            val result = getItems(source, currency, ids)
            if (emitter.isDisposed) {
                Timber.v("Firestore emitter disposed")
                return@create
            }
            if (result == null) {
                emitter.onComplete()
            } else {
                Timber.v("Firestore Result %d", result.size)
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
        val collection = Constants.FirebaseKey.CRYPTO
        val document = coin.id
        val paths = TreeSet<MutablePair<String, String>>()
        paths.add(MutablePair.of(coin.source.name, Constants.FirebaseKey.COINS))

        val error = firestore.setItemRx(collection, paths, document, coin).blockingGet()
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
                throw IllegalStateException()
            }
            if (result == -1L) {
                emitter.onComplete()
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun putItems(coins: List<Coin>): List<Long>? {
        val collection = Constants.FirebaseKey.CRYPTO
        val items = Maps.newHashMap<String, MutableTriple<String, String, Coin>>()
        for (coin in coins) {
            items[coin.id] =
                MutableTriple.of(coin.source.name, Constants.FirebaseKey.COINS, coin)
        }
        val error = firestore.setItemsRx(collection, items).blockingGet()
        if (error == null) {
            putQuotes(coins)
            return ArrayList()
        }
        return null
    }

    override fun putItemsRx(coins: List<Coin>): Maybe<List<Long>> {
        return Maybe.create { emitter ->
            val result = putItems(coins)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == null) {
                emitter.onComplete()
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

    /* private api */
    private fun getQuotes(source: CoinSource, currency: Currency, ids: List<String>): List<Quote> {
        val collection = Constants.FirebaseKey.CRYPTO

        val paths = TreeSet<MutablePair<String, String>>()
        paths.add(MutablePair.of(source.name, Constants.FirebaseKey.QUOTES))

        val lastUpdated = TimeUtil.currentTime() - Constants.Time.Coin

        val equalTo = ArrayList<MutablePair<String, Any>>()
        val greaterThanOrEqualTo = ArrayList<MutablePair<String, Any>>()

        for (id in ids) {
            equalTo.add(MutablePair.of(Constants.Quote.ID, id))
            equalTo.add(MutablePair.of(Constants.Quote.CURRENCY, currency.name))
        }

        greaterThanOrEqualTo.add(MutablePair.of(Constants.Coin.LAST_UPDATED, lastUpdated))

        return firestore.getItemsRx(
            collection, paths, equalTo, null, greaterThanOrEqualTo,
            Quote::class.java
        ).blockingGet()
    }


    private fun putQuote(coin: Coin): Long {
        val latest = coin.latestQuote
        return if (latest != null) {
            putQuote(coin.source, latest)
        } else 0
    }

    private fun putQuotes(coins: List<Coin>): List<Long>? {
        val collection = Constants.FirebaseKey.CRYPTO
        val items = Maps.newHashMap<String, MutableTriple<String, String, Quote>>()
        for (coin in coins) {
            val latest = coin.latestQuote
            if (latest != null) {
                items[latest.id + latest.currency.name] =
                    MutableTriple.of(coin.source.name, Constants.FirebaseKey.QUOTES, latest)
            }
        }
        if (!items.isEmpty()) {
            val error = firestore.setItemsRx(collection, items).blockingGet() ?: return ArrayList()
        }
        return null
    }


    private fun putQuote(source: CoinSource, quote: Quote): Long {
        val collection = Constants.FirebaseKey.CRYPTO
        val document = quote.id + quote.currency.name
        val paths = TreeSet<MutablePair<String, String>>()
        paths.add(MutablePair.of(source.name, Constants.FirebaseKey.QUOTES))

        val error =
            firestore.setItemRx(collection, paths, document, quote).blockingGet() ?: return 1
        return -1
    }

    private fun bindQuote(currency: Currency, coin: Coin?) {
        if (coin != null && !coin.hasQuote(currency)) {
            //Quote quote = quoteDao.getItemsWithoutId(coin.getId(), currency.name());
            //coin.addQuote(quote);
        }
    }
}