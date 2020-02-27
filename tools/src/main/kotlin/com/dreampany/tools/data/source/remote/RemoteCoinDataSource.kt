package com.dreampany.tools.data.source.remote

import com.dreampany.framework.api.key.KeyManager
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.api.crypto.remote.CoinMarketCapService
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.data.mapper.CoinMapper
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.source.api.CoinDataSource
import com.dreampany.tools.misc.Constants
import com.google.common.collect.Maps
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RemoteCoinDataSource
constructor(
    private val network: NetworkManager,
    private val keyM: KeyManager,
    private val mapper: CoinMapper,
    private val service: CoinMarketCapService
) : CoinDataSource {
    init {
        if (BuildConfig.DEBUG) {
            keyM.setKeys(
                com.dreampany.tools.api.crypto.misc.Constants.CoinMarketCap.CMC_PRO_ROMAN_BJIT
            )
        } else {
            keyM.setKeys(
                com.dreampany.tools.api.crypto.misc.Constants.CoinMarketCap.CMC_PRO_DREAM_DEBUG_2,
                com.dreampany.tools.api.crypto.misc.Constants.CoinMarketCap.CMC_PRO_DREAM_DEBUG_1,
                com.dreampany.tools.api.crypto.misc.Constants.CoinMarketCap.CMC_PRO_ROMAN_BJIT,
                com.dreampany.tools.api.crypto.misc.Constants.CoinMarketCap.CMC_PRO_IFTE_NET,
                com.dreampany.tools.api.crypto.misc.Constants.CoinMarketCap.CMC_PRO_DREAMPANY
            )
        }

    }

    override fun getItem(currency: Currency, id: String): Coin? {
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }

        for (index in 0..keyM.length / 2) {
            try {
                val key = keyM.getKey()
                val response =
                    service.getQuotes(getHeaders(key), currency.name, id).execute()
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null) {
                        val coin = res.data.get(id) ?: return null
                        return mapper.getItem(coin)
                    }
                }
            } catch (error: Throwable) {
                Timber.e(error)
                keyM.randomForwardKey()
            }
        }

        return null
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

    override fun getItems(
        currency: Currency, sort: CoinSort,
        order: Order,
        start: Long, limit: Long
    ): List<Coin>? {
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        for (index in 0..keyM.length / 2) {
            try {
                val key = keyM.getKey()

                val response =
                    service.getListing(
                        getHeaders(key),
                        currency.name,
                        sort.value,
                        order.value,
                        start + 1,
                        limit
                    ).execute()
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null) {
                        return mapper.getItems(res.data)
                    }
                }
            } catch (error: Throwable) {
                Timber.e(error)
                keyM.randomForwardKey()
            }
        }
        return null
    }

    override fun getItemsRx(
        currency: Currency,
        sort: CoinSort,
        order: Order,
        start: Long, limit: Long
    ): Maybe<List<Coin>> {
        return Maybe.create { emitter ->
            val result = getItems(currency, sort, order, start, limit)
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun getItems(currency: Currency, ids: List<String>): List<Coin>? {
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        for (index in 0..keyM.length / 2) {
            try {
                val key = keyM.getKey()
                val response =
                    service.getQuotes(
                        getHeaders(key),
                        currency.name,
                        ids.joinToString(Constants.Sep.COMMA.toString())
                    ).execute()
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null) {
                        return mapper.getItems(res.data.values.toMutableList())
                    }
                }
            } catch (error: Throwable) {
                Timber.e(error)
                keyM.randomForwardKey()
            }
        }
        return null
    }

    override fun getItemsRx(currency: Currency, ids: List<String>): Maybe<List<Coin>> {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
/*        for (index in 0..keyM.length / 2) {
            try {
                val key = keyM.getKey()

                val response =
                    service.getQuotes(
                        getHeaders(key), ).execute()
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null) {
                        return mapper.getItems(res.data)
                    }
                }
            } catch (error: Throwable) {
                Timber.e(error)
                keyM.randomForwardKey()
            }
        }*/
        return null
    }

    override fun getItemRx(id: String): Maybe<Coin> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Coin>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Coin>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /* private */
    fun getHeaders(key: String): Map<String, String> {
        val headers = Maps.newHashMap<String, String>()
        headers.put(
            com.dreampany.tools.api.crypto.misc.Constants.CoinMarketCap.ACCEPT,
            com.dreampany.tools.api.crypto.misc.Constants.CoinMarketCap.ACCEPT_JSON
        )
        //headers.put(Constants.CoinMarketCap.ACCEPT_ENCODING, Constants.CoinMarketCap.ACCEPT_ZIP)
        headers.put(com.dreampany.tools.api.crypto.misc.Constants.CoinMarketCap.API_KEY, key)
        return headers
    }


}