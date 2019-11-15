package com.dreampany.tools.data.source.remote

import com.dreampany.framework.api.key.KeyManager
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.BuildConfig
import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.remote.CoinMarketCapService
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.data.mapper.CoinMapper
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.source.api.CoinDataSource
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
@Singleton
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
                Constants.CoinMarketCap.CMC_PRO_ROMAN_BJIT
            )
        } else {
            keyM.setKeys(
                Constants.CoinMarketCap.CMC_PRO_DREAM_DEBUG_2,
                Constants.CoinMarketCap.CMC_PRO_DREAM_DEBUG_1,
                Constants.CoinMarketCap.CMC_PRO_ROMAN_BJIT,
                Constants.CoinMarketCap.CMC_PRO_IFTE_NET,
                Constants.CoinMarketCap.CMC_PRO_DREAMPANY
            )
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
            if (emitter.isDisposed) {
                return@create
            }
            if (result.isNullOrEmpty()) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        headers.put(Constants.CoinMarketCap.ACCEPT, Constants.CoinMarketCap.ACCEPT_JSON)
        //headers.put(Constants.CoinMarketCap.ACCEPT_ENCODING, Constants.CoinMarketCap.ACCEPT_ZIP)
        headers.put(Constants.CoinMarketCap.API_KEY, key)
        return headers
    }

}