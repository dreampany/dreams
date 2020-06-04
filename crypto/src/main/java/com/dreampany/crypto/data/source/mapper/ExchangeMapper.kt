package com.dreampany.crypto.data.source.mapper

import com.dreampany.crypto.api.model.CryptoExchange
import com.dreampany.crypto.data.model.Exchange
import com.dreampany.crypto.data.source.pref.AppPref
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.google.common.collect.Maps
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ExchangeMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: AppPref
) {
    private val exchanges: MutableMap<String, Exchange>

    init {
        exchanges = Maps.newConcurrentMap()
    }

    @Synchronized
    fun add(exchange: Exchange) = exchanges.put(exchange.id, exchange)

    @Synchronized
    fun getItems(inputs: List<CryptoExchange>): List<Exchange> {
        val result = arrayListOf<Exchange>()
        inputs.forEach { input ->
            result.add(getItem(input))
        }
        return result
    }

    @Synchronized
    fun getItem(input: CryptoExchange): Exchange {
        Timber.v("Resolved Exchange: %s", input.market);
        val id: String = input.let { it.market + it.fromSymbol + it.toSymbol }
        var out: Exchange? = exchanges.get(id)
        if (out == null) {
            out = Exchange(id)
            exchanges.put(id, out)
        }
        out.market = input.market
        out.setFromSymbol(input.fromSymbol)
        out.setToSymbol(input.toSymbol)
        out.price = input.price
        out.setVolume24h(input.volume24h)
        out.setChange24h(input.change24h)
        out.setChangePct24h(input.changePct24h)
        return out
    }

}