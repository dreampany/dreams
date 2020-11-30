package com.dreampany.crypto.data.source.mapper

import com.dreampany.crypto.api.crypto.model.gecko.GeckoConvertedLast
import com.dreampany.crypto.api.crypto.model.gecko.GeckoConvertedVolume
import com.dreampany.crypto.api.crypto.model.gecko.GeckoMarket
import com.dreampany.crypto.api.crypto.model.gecko.GeckoTicker
import com.dreampany.crypto.data.model.ConvertedLast
import com.dreampany.crypto.data.model.ConvertedVolume
import com.dreampany.crypto.data.model.Market
import com.dreampany.crypto.data.model.Ticker
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.utc
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class TickerMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo
) {
    private val tickers: MutableMap<String, Ticker>
    private val markets: MutableMap<String, Market>

    init {
        tickers = Maps.newConcurrentMap()
        markets = Maps.newConcurrentMap()
    }

    @Synchronized
    fun add(input: Ticker) = tickers.put(input.id, input)

    @Synchronized
    fun add(input: Market) = markets.put(input.id, input)

    @Synchronized
    fun getItems(inputs: List<GeckoTicker>): List<Ticker> {
        val result = arrayListOf<Ticker>()
        inputs.forEach { input ->
            result.add(input.ticker)
        }
        return result
    }

    private val GeckoTicker.ticker: Ticker
        get() {
            val id: String = market.id + base + target
            var out: Ticker? = tickers.get(id)
            if (out == null) {
                out = Ticker(id)
                tickers.put(id, out)
            }
            out.base = base
            out.target = target
            out.market = market.market
            out.last = last
            out.volume = volume
            out.convertedLast = convertedLast.last
            out.convertedVolume = convertedVolume.volume
            out.timestamp = timestamp.utc
            out.lastTradedAt = lastTradedAt.utc
            out.lastFetchAt = lastFetchAt.utc
            return out
        }

    private val GeckoMarket.market: Market
        get() {
            var out: Market? = markets.get(id)
            if (out == null) {
                out = Market(id, name, image)
                markets.put(id, out)
            }
            return out
        }

    private val GeckoConvertedLast.last: ConvertedLast
        get() = ConvertedLast(btc, eth, usd)

    private val GeckoConvertedVolume.volume: ConvertedVolume
        get() = ConvertedVolume(btc, eth, usd)

}