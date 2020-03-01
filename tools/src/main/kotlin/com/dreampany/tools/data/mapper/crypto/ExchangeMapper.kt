package com.dreampany.tools.data.mapper.crypto

import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.api.crypto.model.CryptoExchange
import com.dreampany.tools.data.model.crypto.Exchange
import com.dreampany.tools.injector.annote.crypto.ExchangeAnnote
import com.dreampany.tools.injector.annote.crypto.ExchangeItemAnnote
import com.dreampany.tools.ui.model.crypto.ExchangeItem
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
class ExchangeMapper
@Inject constructor(
    @ExchangeAnnote private val map: SmartMap<String, Exchange>,
    @ExchangeAnnote private val cache: SmartCache<String, Exchange>,
    @ExchangeItemAnnote private val uiMap: SmartMap<String, ExchangeItem>,
    @ExchangeItemAnnote private val uiCache: SmartCache<String, ExchangeItem>
){
    fun getUiItem(id: String): ExchangeItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: ExchangeItem) {
        uiMap.put(id, uiItem)
    }

    fun getItems(inputs: List<CryptoExchange>): List<Exchange> {
        val result = arrayListOf<Exchange>()
        inputs.forEach { item ->
            result.add(getItem(item))
        }
        return result
    }

    fun getItem(input: CryptoExchange): Exchange {
        Timber.v("Resolved Exchange: %s %s %s", input.market, input.fromSymbol, input.toSymbol);
        val id = input.market.plus(input.fromSymbol).plus(input.toSymbol)
        var out: Exchange? = map.get(id)
        if (out == null) {
            out = Exchange(id)
            map.put(id, out)
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