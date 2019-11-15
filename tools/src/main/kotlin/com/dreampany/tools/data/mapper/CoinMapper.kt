package com.dreampany.tools.data.mapper

import android.content.Context
import androidx.core.util.Pair
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.util.DataUtilKt
import com.dreampany.framework.util.TimeUtil
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.model.Quote
import com.dreampany.tools.data.source.api.CoinDataSource
import com.dreampany.tools.data.source.pref.CryptoPref
import com.dreampany.tools.data.source.room.dao.QuoteDao
import com.dreampany.tools.injector.annotation.CoinAnnote
import com.dreampany.tools.injector.annotation.CoinItemAnnote
import com.dreampany.tools.injector.annotation.CurrencyAnnote
import com.dreampany.tools.injector.annotation.QuoteAnnote
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.CoinItem
import com.google.common.collect.Maps
import io.reactivex.Maybe
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

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
    private val pref: CryptoPref,
    @CoinAnnote private val map: SmartMap<String, Coin>,
    @CoinAnnote private val cache: SmartCache<String, Coin>,
    @CoinItemAnnote private val uiMap: SmartMap<String, CoinItem>,
    @CoinItemAnnote private val uiCache: SmartCache<String, CoinItem>,
    @CurrencyAnnote private val currencyMap: SmartMap<String, Currency>,
    @CurrencyAnnote private val currencyCache: SmartCache<String, Currency>,
    @QuoteAnnote private val quoteMap: SmartMap<Pair<String, Currency>, Quote>,
    @QuoteAnnote private val quoteCache: SmartCache<Pair<String, Currency>, Quote>
) {

    private val coins: MutableList<Coin>

    init {
        coins = Collections.synchronizedList(ArrayList<Coin>())
    }

    fun isExpired(currency: Currency, sort: CoinSort, order: Order, start: Long): Boolean {
        val time = pref.getExpireTime(currency, sort, order, start)
        return TimeUtil.isExpired(time, Constants.Time.Coin.LISTING)
    }

    fun commitExpire(currency: Currency, sort: CoinSort, order: Order, start: Long) {
        pref.getExpireTime(currency, sort, order, start)
    }

    fun getUiItem(id: String): CoinItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: CoinItem) {
        uiMap.put(id, uiItem)
    }

    fun getItemsRx(
        currency: Currency,
        sort: CoinSort,
        sortDirection: Order,
        start: Long,
        limit: Long,
        quoteDao: QuoteDao,
        source: CoinDataSource
    ): Maybe<List<Coin>> {
        return Maybe.create { emitter ->
            updateCache(source)
            val cache = sortedCoins(currency, sort, sortDirection)
            val result = DataUtilKt.sub(cache, start.toInt(), limit.toInt())
            result?.forEach {
                bindQuote(currency, it, quoteDao)
            }
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

    fun getItems(inputs: List<com.dreampany.tools.api.crypto.model.Coin>): List<Coin> {
        val result = arrayListOf<Coin>()
        inputs.forEach { coin ->
            result.add(getItem(coin))
        }
        return result
    }

    fun getItem(input: com.dreampany.tools.api.crypto.model.Coin): Coin {

        Timber.v("Resolved Coin: %s", input.name);

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

    private fun bindQuote(currency: Currency, coin: Coin, dao: QuoteDao) {
        if (!coin.hasQuote(currency)) {
            dao.getItem(coin.id, currency.name)?.run {
                coin.addQuote(this)
            }
        }
    }

    private fun getUtc(time: String): Long {
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

    @Synchronized
    private fun sortedCoins(
        currency: Currency,
        sort: CoinSort,
        sortDirection: Order
    ): List<Coin> {
        //val result = ArrayList(coins)
        val comparator =
            Constants.Comparator.Crypto.getComparator(currency, sort, sortDirection)
        coins.sortWith(comparator)
        return coins
    }


}