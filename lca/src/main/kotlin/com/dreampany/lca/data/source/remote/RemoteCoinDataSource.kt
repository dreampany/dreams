package com.dreampany.lca.data.source.remote

import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.NumberUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.lca.BuildConfig
import com.dreampany.lca.api.cmc.model.CmcListingResponse
import com.dreampany.lca.api.cmc.model.CmcQuotesResponse
import com.dreampany.lca.data.enums.CoinSource
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.misc.CoinMapper
import com.dreampany.lca.data.model.Coin
import com.dreampany.lca.data.source.api.CoinDataSource
import com.dreampany.lca.misc.Constants
import com.dreampany.network.manager.NetworkManager
import com.google.common.collect.Maps
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.apache.commons.collections4.queue.CircularFifoQueue
import org.apache.commons.lang3.tuple.MutablePair
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RemoteCoinDataSource
constructor(
    val network: NetworkManager,
    val mapper: CoinMapper,
    val service: CmcService
) : CoinDataSource {
    private val keys: MutableList<String>
    private val indexQueue: CircularFifoQueue<Int>
    private val indexStatus: MutableMap<Int, MutablePair<Long, Int>>

    init {
        keys = Collections.synchronizedList(ArrayList())

        keys.add(Constants.ApiKey.CMC_PRO_DREAM_DEBUG_2)
        if (!BuildConfig.DEBUG) {
            keys.add(Constants.ApiKey.CMC_PRO_DREAM_DEBUG_1)
            keys.add(Constants.ApiKey.CMC_PRO_ROMAN_BJIT)
            keys.add(Constants.ApiKey.CMC_PRO_IFTE_NET)
            keys.add(Constants.ApiKey.CMC_PRO_DREAMPANY)
        }

        indexQueue = CircularFifoQueue(keys.size)
        indexStatus = Maps.newConcurrentMap()
        for (index in keys.indices) {
            indexQueue.add(index)
            indexStatus[index] = MutablePair.of(TimeUtil.currentTime(), 0)
        }
        var randIndex = NumberUtil.nextRand(keys.size)
        while (randIndex > 0) {
            iterateQueue()
            randIndex--
        }
    }

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
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        val start = index + 1
        for (loop in keys.indices) {
            val apiKey = getApiKey()
            try {
                val response = service.getListing(apiKey, currency.name, start, limit).execute()
                if (response.isSuccessful) {
                    val result = response.body()
                    return getItemsRx(source, result)!!.blockingGet()
                }
            } catch (e: IOException) {
                Timber.e(e)
                iterateQueue()
            } catch (e: RuntimeException) {
                Timber.e(e)
                iterateQueue()
            }

        }
        return null
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
        return null
    }

    override fun getItemsRx(source: CoinSource, currency: Currency): Maybe<List<Coin>> {
        return Maybe.empty()
    }

    override fun getItems(source: CoinSource, currency: Currency, limit: Int): List<Coin>? {
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        val start = 1 // start from 1 index
        for (loop in keys.indices) {
            val apiKey = getApiKey()
            try {
                val response = service.getListing(apiKey, currency.name, start, limit).execute()
                if (response.isSuccessful) {
                    val result = response.body()
                    return getItemsRx(source, result)!!.blockingGet()
                }
            } catch (e: IOException) {
                Timber.e(e)
                iterateQueue()
            } catch (e: RuntimeException) {
                Timber.e(e)
                iterateQueue()
            }

        }
        return null
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
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        Timber.v("Ids %s", id)
        for (loop in keys.indices) {
            val apiKey = getApiKey()
            try {
                val response = service.getQuotesByIds(apiKey, currency.name, id).execute()
                if (response.isSuccessful) {
                    val result = response.body()
                    return getItemRx(source, result!!)!!.blockingGet()
                }
            } catch (e: IOException) {
                Timber.e(e)
                iterateQueue()
            } catch (e: RuntimeException) {
                Timber.e(e)
                iterateQueue()
            }

        }
        return null
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

    override fun getItems(
        source: CoinSource,
        currency: Currency,
        coinIds: List<String>
    ): List<Coin>? {
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        val ids = mapper.joinLongToString(coinIds, Constants.Sep.COMMA)
        Timber.v("Ids %s", ids)
        for (loop in keys.indices) {
            val apiKey = getApiKey()
            try {
                val response = service.getQuotesByIds(apiKey, currency.name, ids).execute()
                if (response.isSuccessful) {
                    val result = response.body()
                    return getItemsRx(source, result)!!.blockingGet()
                }
            } catch (e: IOException) {
                Timber.e(e)
                iterateQueue()
            } catch (e: RuntimeException) {
                Timber.e(e)
                iterateQueue()
            }

        }
        return null
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
                emitter.onComplete()
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
        return 0
    }

    override fun putItemRx(coin: Coin): Maybe<Long> {
        return Maybe.empty()
    }

    override fun putItems(coins: List<Coin>): List<Long>? {
        return null
    }

    override fun putItemsRx(coins: List<Coin>): Maybe<List<Long>> {
        return Maybe.empty()
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
    private fun getApiKey(): String {
        adjustIndexStatus()
        return keys[indexQueue.peek()!!]
    }

    private fun iterateQueue() {
        indexQueue.add(indexQueue.peek())
    }

    private fun adjustIndexStatus() {
        val index = indexQueue.peek()!!
        val pair = indexStatus[index]
        pair?.let {
            if (TimeUtil.isExpired(it.left, Constants.Delay.CmcKey)) {
                it.setLeft(TimeUtil.currentTime())
                it.setRight(0)
            }
            if (it.right > Constants.Limit.CMC_KEY) {
                iterateQueue()
            }
            it.right++
        }
    }

    /* private api*/
    private fun getItemsRx(source: CoinSource, response: CmcListingResponse?): Maybe<List<Coin>>? {
        if (response == null || response.hasError() || !response.hasData()) {
            return null
        }
        val items = response.data
        return Flowable.fromIterable(items)
            .map { `in` -> mapper.toItem(source, `in`, true) }
            //.toList()
            .toSortedList { left, right -> left.rank - right.rank }
            .toMaybe()
    }

    private fun getItemsRx(source: CoinSource, response: CmcQuotesResponse?): Maybe<List<Coin>>? {
        if (response == null || response.hasError() || !response.hasData()) {
            return null
        }
        val items = response.data.values
        return Flowable.fromIterable(items)
            .map { `in` -> mapper.toItem(source, `in`, true) }
            .toSortedList { left, right -> left.rank - right.rank }
            .toMaybe()
    }

    private fun getItemRx(source: CoinSource, response: CmcQuotesResponse): Maybe<Coin>? {
        if (response.hasError() || !response.hasData()) {
            return null
        }
        val item = response.first
        return Maybe.just(item).map { `in` -> mapper.toItem(source, `in`, true) }
    }
}