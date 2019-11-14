package com.dreampany.tools.data.mapper

import android.content.Context
import androidx.core.util.Pair
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.util.TimeUtil
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.model.Quote
import com.dreampany.tools.data.source.api.CoinDataSource
import com.dreampany.tools.data.source.pref.CoinPref
import com.dreampany.tools.injector.annotation.CoinAnnote
import com.dreampany.tools.injector.annotation.CurrencyAnnote
import com.dreampany.tools.injector.annotation.QuoteAnnote
import com.dreampany.tools.misc.Constants
import com.google.common.collect.Maps
import io.reactivex.Maybe
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CoinMapper
@Inject constructor(
    private val context: Context,
    private val pref: CoinPref,
    @CoinAnnote private val map: SmartMap<String, Coin>,
    @CoinAnnote private val cache: SmartCache<String, Coin>,
    @CurrencyAnnote private val currencyMap: SmartMap<String, Currency>,
    @CurrencyAnnote private val currencyCache: SmartCache<String, Currency>,
    @QuoteAnnote private val quoteMap: SmartMap<Pair<String, Currency>, Quote>,
    @QuoteAnnote private val quoteCache: SmartCache<Pair<String, Currency>, Quote>
) {

    private val coins: MutableList<Coin>

    init {
        coins = Collections.synchronizedList(ArrayList<Coin>())
    }

    fun isExpired(currency: Currency, sort: CoinSort, start: Long): Boolean {
        val time = pref.getExpireTime(currency.name, sort.value, start)
        return TimeUtil.isExpired(time, Constants.Time.Coin.LISTING)
    }

    fun commitExpire(currency: Currency, sort: String, start: Long) {
        pref.getExpireTime(currency.name, sort, start)
    }

    fun getItemsRx(
        source: CoinDataSource, currency: Currency,
        sort: String,
        sortDirection: String,
        auxiliaries: String,
        start: Long,
        limit: Long
    ): Maybe<List<Coin>> {
        return Maybe.create { emitter ->
            updateCache(source)
            /*val result = getItems(source, currency, index, limit)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }*/
        }
    }

    fun getItems(inputs: List<com.dreampany.tools.api.crypto.model.Coin>): List<Coin> {
        val result = arrayListOf<Coin>()
        inputs.forEach { coin ->
            result.add(getItem(coin))
        }
        return result
    }

    fun getItem(input: com.dreampany.tools.api.crypto.model.Coin): Coin {

        val id = input.id.toString()
        var out: Coin? = map.get(id)
        if (out == null) {
            out = Coin(id)
            map.put(id, out)
        }
        out.name = input.name
        out.symbol = input.symbol
        out.slug = input.slug
        out.setCirculatingSupply(input.circulatingSupply)
        out.setMaxSupply(input.maxSupply)
        out.setTotalSupply(input.totalSupply)
        out.setMarketPairs(input.marketPairs)
        out.rank = input.rank
        out.tags = input.tags
        out.quotes = getQuotes(id, input.quotes)

        return out
    }

    fun getQuotes(
        coinId: String,
        input: Map<com.dreampany.tools.api.crypto.model.Currency, com.dreampany.tools.api.crypto.model.Quote>
    ): HashMap<Currency, Quote> {
        val result = Maps.newHashMap<Currency, Quote>()
        input.forEach { entry ->
            val currency = getCurrency(entry.key)
            result.put(currency, getQuote(coinId, currency, entry.value))
        }
        return result
    }

    fun getCurrency(input: com.dreampany.tools.api.crypto.model.Currency): Currency {
        var out: Currency? = currencyMap.get(input.name)
        if (out == null) {
            out = Currency.valueOf(input.name)
            currencyMap.put(input.name, out)
        }
        return out
    }

    fun getQuote(
        coinId: String,
        currency: Currency,
        input: com.dreampany.tools.api.crypto.model.Quote
    ): Quote {
        val id = Pair.create<String, Currency>(coinId, currency)
        var out: Quote? = quoteMap.get(id)
        if (out == null) {
            out = Quote(coinId)
            quoteMap.put(id, out)
        }
        out.currency = currency
        out.price = input.price
        out.setVolume24h(input.volume24h)
        out.setMarketCap(input.marketCap)
        out.setChange1h(input.change1h)
        out.setChange24h(input.change24h)
        out.setChange7d(input.change7d)
        out.setLastUpdated(getUtc(input.lastUpdated))

        return out
    }

    fun getUtc(time: String): Long {
        return TimeUtil.getUtcTime(time)
    }

    @Synchronized
    private fun updateCache(source: CoinDataSource) {
        if (source.getCount() != coins.size) {
            source.getItems()?.forEach {
                if (!coins.contains(it)) {
                    coins.add(it)
                }
            }
        }
    }

    private fun sortedCoins(sort: String, sortDirection: String): List<Coin> {
        val result = ArrayList<Coin>(coins)

        return result
    }


}