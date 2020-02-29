package com.dreampany.tools.data.mapper.crypto

import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.api.crypto.model.CryptoTrade
import com.dreampany.tools.data.model.crypto.Trade
import com.dreampany.tools.injector.annote.crypto.TradeAnnote
import com.dreampany.tools.injector.annote.crypto.TradeItemAnnote
import com.dreampany.tools.ui.model.crypto.TradeItem
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class TradeMapper
@Inject constructor(
    @TradeAnnote private val map: SmartMap<String, Trade>,
    @TradeAnnote private val cache: SmartCache<String, Trade>,
    @TradeItemAnnote private val uiMap: SmartMap<String, TradeItem>,
    @TradeItemAnnote private val uiCache: SmartCache<String, TradeItem>
){
    fun getUiItem(id: String): TradeItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: TradeItem) {
        uiMap.put(id, uiItem)
    }

    fun getItems(inputs: List<CryptoTrade>): List<Trade> {
        val result = arrayListOf<Trade>()
        inputs.forEach { item ->
            result.add(getItem(item))
        }
        return result
    }

    fun getItem(input: CryptoTrade): Trade {
        Timber.v("Resolved Trade: %s %s %s", input.exchange, input.fromSymbol, input.toSymbol);
        val id = input.exchange.plus(input.fromSymbol).plus(input.toSymbol)
        var out: Trade? = map.get(id)
        if (out == null) {
            out = Trade(id)
            map.put(id, out)
        }
        out.exchange = input.exchange
        out.setFromSymbol(input.fromSymbol)
        out.setToSymbol(input.toSymbol)
        out.setVolume24h(input.volume24h)
        out.setVolume24hTo(input.volume24hTo)
        return out
    }
}