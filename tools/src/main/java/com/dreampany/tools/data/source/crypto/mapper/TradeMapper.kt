package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.tools.api.crypto.model.CryptoTrade
import com.dreampany.tools.data.model.crypto.Trade
import com.dreampany.tools.data.source.crypto.pref.Prefs
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
class TradeMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: Prefs
) {
    private val trades: MutableMap<String, Trade>

    init {
        trades = Maps.newConcurrentMap()
    }

    @Synchronized
    fun add(trade: Trade) = trades.put(trade.id, trade)

    @Synchronized
    fun getItems(inputs: List<CryptoTrade>): List<Trade> {
        val result = arrayListOf<Trade>()
        inputs.forEach { input ->
            result.add(getItem(input))
        }
        return result
    }

    @Synchronized
    fun getItem(input: CryptoTrade): Trade {
        Timber.v("Resolved Trade: %s", input.exchange);
        val id: String = input.let { it.exchange + it.fromSymbol + it.toSymbol }
        var out: Trade? = trades.get(id)
        if (out == null) {
            out = Trade(id)
            trades.put(id, out)
        }
        out.exchange = input.exchange
        out.setFromSymbol(input.fromSymbol)
        out.setToSymbol(input.toSymbol)
        out.setVolume24h(input.volume24h)
        out.setVolume24hTo(input.volume24hTo)
        return out
    }

}