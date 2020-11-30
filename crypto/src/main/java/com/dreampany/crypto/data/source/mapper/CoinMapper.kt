package com.dreampany.crypto.data.source.mapper

import com.dreampany.crypto.api.crypto.model.CryptoCoin
import com.dreampany.crypto.api.crypto.model.CryptoCurrency
import com.dreampany.crypto.api.crypto.model.CryptoQuote
import com.dreampany.crypto.data.enums.*
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.data.model.Quote
import com.dreampany.crypto.data.source.api.CoinDataSource
import com.dreampany.crypto.data.source.pref.AppPref
import com.dreampany.crypto.data.source.room.dao.QuoteDao
import com.dreampany.crypto.misc.constants.Constants
import com.dreampany.framework.data.enums.Order
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.framework.misc.exts.sub
import com.dreampany.framework.misc.exts.value
import com.google.common.collect.Maps
import timber.log.Timber
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
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: AppPref
) {
    private val coins: MutableMap<String, Coin>
    private val quotes: MutableMap<Pair<String, Currency>, Quote>
    private val currencies: MutableMap<String, Currency>
    private val favorites: MutableMap<String, Boolean>

    init {
        coins = Maps.newConcurrentMap()
        quotes = Maps.newConcurrentMap()
        currencies = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    @Synchronized
    fun isExpired(currency: Currency, sort: Sort, order: Order, offset: Long): Boolean {
        val time = pref.getExpireTime(currency, sort, order, offset)
        return time.isExpired(Constants.Times.COINS)
    }

    @Synchronized
    fun commitExpire(currency: Currency, sort: Sort, order: Order, offset: Long) =
        pref.commitExpireTime(currency, sort, order, offset)

    @Synchronized
    fun isExpired(id: String, currency: Currency): Boolean {
        val time = pref.getExpireTime(id, currency)
        return time.isExpired(Constants.Times.COIN)
    }

    @Synchronized
    fun commitExpire(id: String, currency: Currency) =
        pref.commitExpireTime(id, currency)


    @Synchronized
    fun add(input: Coin) = coins.put(input.id, input)

    @Throws
    suspend fun isFavorite(input: Coin): Boolean {
        if (!favorites.containsKey(input.id)) {
            val favorite = storeRepo.isExists(
                input.id,
                Type.COIN.value,
                Subtype.DEFAULT.value,
                State.FAVORITE.value
            )
            favorites.put(input.id, favorite)
        }
        return favorites.get(input.id).value
    }

    @Throws
    suspend fun insertFavorite(input: Coin): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.readStore(
            input.id,
            Type.COIN.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.write(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: Coin): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.readStore(
            input.id,
            Type.COIN.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.delete(it) }
        return false
    }

    @Throws
    @Synchronized
    suspend fun getItems(
        currency: Currency,
        sort: Sort,
        sortDirection: Order,
        offset: Long,
        limit: Long,
        quoteDao: QuoteDao,
        source: CoinDataSource
    ): List<Coin>? {
        updateCache(source)
        val cache = sortedCoins(coins.values.toList(), currency, sort, sortDirection)
        val result = sub(cache, offset, limit)
        result?.forEach {
            bindQuote(currency, it, quoteDao)
        }
        return result
    }

    @Throws
    @Synchronized
    suspend fun getItem(
        id: String,
        currency: Currency,
        quoteDao: QuoteDao,
        source: CoinDataSource
    ): Coin? {
        updateCache(source)
        val result = coins.get(id)
        result?.let {
            bindQuote(currency, it, quoteDao)
        }
        return result
    }

    @Throws
    @Synchronized
    suspend fun getFavoriteItems(
        currency: Currency,
        sort: Sort,
        sortDirection: Order,
        quoteDao: QuoteDao,
        source: CoinDataSource
    ): List<Coin>? {
        updateCache(source)
        val stores = storeRepo.reads(
            Type.COIN.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        val outputs = stores?.mapNotNull { input -> coins.get(input.id) }
        var result: List<Coin>? = null
        outputs?.let {
            result = sortedCoins(it, currency, sort, sortDirection)
        }
        result?.forEach {
            bindQuote(currency, it, quoteDao)
        }
        return result
    }

    @Synchronized
    fun getItems(inputs: List<CryptoCoin>): List<Coin> {
        val result = arrayListOf<Coin>()
        inputs.forEach { input ->
            result.add(getItem(input))
        }
        return result
    }

    @Synchronized
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
        out.setDateAdded(input.dateAdded.utc)
        out.setLastUpdated(input.lastUpdated.utc)
        return out
    }

    @Synchronized
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

    @Synchronized
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
        out.setLastUpdated(input.lastUpdated.utc)

        return out
    }

    @Synchronized
    fun getCurrency(input: CryptoCurrency): Currency {
        var out: Currency? = currencies.get(input.name)
        if (out == null) {
            out = Currency.valueOf(input.name)
            currencies.put(input.name, out)
        }
        return out
    }

    @Synchronized
    private fun bindQuote(currency: Currency, item: Coin, dao: QuoteDao) {
        if (!item.hasQuote(currency)) {
            dao.getItem(item.id, currency.name)?.let { item.addQuote(it) }
        }
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: CoinDataSource) {
        if (coins.isEmpty()) {
            source.gets()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sortedCoins(
        inputs: List<Coin>,
        currency: Currency,
        sort: Sort,
        order: Order
    ): List<Coin> {
        val temp = ArrayList(inputs)
        val comparator = CryptoComparator(currency, sort, order)
        temp.sortWith(comparator)
        return temp
    }

    class CryptoComparator(
        private val currency: Currency,
        private val sort: Sort,
        private val order: Order
    ) : Comparator<Coin> {
        override fun compare(left: Coin, right: Coin): Int {
            if (sort == Sort.MARKET_CAP) {
                val leftCap = left.getQuote(currency)
                val rightCap = right.getQuote(currency)
                if (leftCap != null && rightCap != null) {
                    if (order == Order.ASCENDING) {
                        return leftCap.getMarketCap().compareTo(rightCap.getMarketCap())
                    } else {
                        return rightCap.getMarketCap().compareTo(leftCap.getMarketCap())
                    }
                }
            }
            return 0
        }
    }

}