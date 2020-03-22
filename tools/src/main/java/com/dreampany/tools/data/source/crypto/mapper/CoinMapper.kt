package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.common.data.enums.Order
import com.dreampany.common.misc.extension.isExpired
import com.dreampany.common.misc.extension.sub
import com.dreampany.common.misc.extension.utc
import com.dreampany.tools.api.crypto.model.CryptoCoin
import com.dreampany.tools.api.crypto.model.CryptoCurrency
import com.dreampany.tools.api.crypto.model.CryptoQuote
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao
import com.dreampany.tools.misc.comparator.Comparators
import com.dreampany.tools.misc.constant.AppConstants
import com.google.common.collect.Maps
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CoinMapper
@Inject constructor(
    private val pref: CryptoPref
) {
    private val coins: MutableMap<String, Coin>
    private val quotes: MutableMap<Pair<String, Currency>, Quote>
    private val currencies: MutableMap<String, Currency>

    init {
        coins = Maps.newConcurrentMap()
        quotes = Maps.newConcurrentMap()
        currencies = Maps.newConcurrentMap()
    }

    fun isExpired(currency: Currency, sort: CoinSort, order: Order, start: Long): Boolean {
        val time = pref.getExpireTime(currency, sort, order, start)
        return time.isExpired(AppConstants.Times.Crypto.LISTING)
    }

    fun commitExpire(currency: Currency, sort: CoinSort, order: Order, start: Long) {
        pref.commitExpireTime(currency, sort, order, start)
    }

    @Synchronized
    fun add(coin: Coin) {
        coins.put(coin.id, coin)
    }

    @Throws
    suspend fun getItems(
        currency: Currency,
        sort: CoinSort,
        sortDirection: Order,
        offset: Long,
        limit: Long,
        quoteDao: QuoteDao,
        source: CoinDataSource
    ): List<Coin>? {
        updateCache(source)
        val cache = sortedCoins(currency, sort, sortDirection)
        val result = sub(cache, offset, limit)
        result?.forEach {
            bindQuote(currency, it, quoteDao)
        }
        return result
    }

    fun getItems(inputs: List<CryptoCoin>): List<Coin> {
        val result = arrayListOf<Coin>()
        inputs.forEach { coin ->
            result.add(getItem(coin))
        }
        return result
    }

    fun getItem(input: CryptoCoin): Coin {

        Timber.v("Resolved Coin: %s", input.name);

        val id = input.id.toString()
        var out: Coin? = coins.get(id)
        if (out == null) {
            out = Coin(id)
            coins.put(id, out)
        }
        out.name = input.name
        out.symbol = input.symbol
        out.slug = input.slug
        out.setCirculatingSupply(input.circulatingSupply)
        out.setMaxSupply(input.maxSupply)
        out.setTotalSupply(input.totalSupply)
        out.setMarketPairs(input.marketPairs)
        out.rank = input.rank
        out.quotes = getQuotes(id, input.quotes)
        out.tags = input.tags
        out.setDateAdded(input.dateAdded.utc())
        out.setLastUpdated(input.lastUpdated.utc())
        return out
    }

    fun getQuotes(
        coinId: String,
        input: Map<CryptoCurrency, CryptoQuote>
    ): HashMap<Currency, Quote> {
        val result = Maps.newHashMap<Currency, Quote>()
        input.forEach { entry ->
            val currency = getCurrency(entry.key)
            result.put(currency, getQuote(coinId, currency, entry.value))
        }
        return result
    }

    fun getQuote(
        coinId: String,
        currency: Currency,
        input: CryptoQuote
    ): Quote {
        val id = Pair(coinId, currency)
        var out: Quote? = quotes.get(id)
        if (out == null) {
            out = Quote(coinId)
            quotes.put(id, out)
        }
        out.currency = currency
        out.price = input.price
        out.setVolume24h(input.volume24h)
        out.setMarketCap(input.marketCap)
        out.setChange1h(input.change1h)
        out.setChange24h(input.change24h)
        out.setChange7d(input.change7d)
        out.setLastUpdated(input.lastUpdated.utc())

        return out
    }

    fun getCurrency(input: CryptoCurrency): Currency {
        var out: Currency? = currencies.get(input.name)
        if (out == null) {
            out = Currency.valueOf(input.name)
            currencies.put(input.name, out)
        }
        return out
    }

    private fun bindQuote(currency: Currency, item: Coin, dao: QuoteDao) {
        if (!item.hasQuote(currency)) {
            dao.getItem(item.id, currency.name)?.let { item.addQuote(it) }
        }
    }

    @Throws
    private suspend fun updateCache(source: CoinDataSource) {
        if (coins.isEmpty()) {
            source.getItems()?.forEach { add(it) }
        }
    }

    private fun sortedCoins(
        currency: Currency,
        sort: CoinSort,
        sortDirection: Order
    ): List<Coin> {
        val temp = ArrayList(coins.values)
        val comparator = Comparators.Crypto.getComparator(currency, sort, sortDirection)
        temp.sortWith(comparator)
        return temp
    }

}