package com.dreampany.tools.data.source.crypto.mapper

import android.content.Context
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.framework.misc.exts.sub
import com.dreampany.framework.misc.exts.utc
import com.dreampany.framework.misc.exts.value
import com.dreampany.tools.R
import com.dreampany.tools.api.crypto.model.cmc.CryptoCoin
import com.dreampany.tools.api.crypto.model.CryptoCurrency
import com.dreampany.tools.api.crypto.model.CryptoQuote
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.misc.constants.CryptoConstants
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
    private val context: Context,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: Prefs
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
    fun isExpired(currency: Currency, sort: String, order: String, offset: Long): Boolean {
        val time = pref.readExpireTime(currency, sort, order, offset)
        return time.isExpired(Constants.Times.Crypto.COINS)
    }

    @Synchronized
    fun writeExpire(currency: Currency, sort: String, order: String, offset: Long) =
        pref.writeExpireTime(currency, sort, order, offset)

    @Synchronized
    fun isExpired(id: String, currency: Currency): Boolean {
        val time = pref.readExpireTime(currency, id)
        return time.isExpired(CryptoConstants.Times.Crypto.COIN)
    }

    @Synchronized
    fun writeExpire(id: String, currency: Currency) =
        pref.writeExpireTime(currency, id)


    @Synchronized
    fun add(input: Coin) = coins.put(input.id, input)

    @Throws
    suspend fun isFavorite(coin: Coin): Boolean {
        if (!favorites.containsKey(coin.id)) {
            val favorite = storeRepo.isExists(
                coin.id,
                Type.COIN.value,
                Subtype.DEFAULT.value,
                State.FAVORITE.value
            )
            favorites.put(coin.id, favorite)
        }
        return favorites.get(coin.id).value
    }

    @Throws
    suspend fun writeFavorite(coin: Coin): Boolean {
        favorites.put(coin.id, true)
        val store = storeMapper.readStore(
            coin.id,
            Type.COIN.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.write(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(coin: Coin): Boolean {
        favorites.put(coin.id, false)
        val store = storeMapper.readStore(
            coin.id,
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
        sort: String,
        order: String,
        offset: Long,
        limit: Long,
        quoteDao: QuoteDao,
        source: CoinDataSource
    ): List<Coin>? {
        updateCache(source)
        val cache = sortedCoins(currency, coins.values.toList(), sort, order)
        val result = sub(cache, offset, limit)
        result?.forEach {
            bindQuote(currency, it, quoteDao)
        }
        return result
    }

    @Throws
    @Synchronized
    suspend fun getItem(
        currency: Currency,
        id: String,
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
        sort: String,
        order: String,
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
            result = sortedCoins(currency, it, sort, order)
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
        val id = input.id
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
            source.reads()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sortedCoins(
        currency: Currency,
        inputs: List<Coin>,
        sort: String,
        order: String
    ): List<Coin> {
        val temp = ArrayList(inputs)
        val comparator = CryptoComparator(context, currency, sort, order)
        temp.sortWith(comparator)
        return temp
    }

    class CryptoComparator(
        private val context: Context,
        private val currency: Currency,
        private val sort: String,
        private val order: String
    ) : Comparator<Coin> {

        val String.isMarketCap: Boolean
            get() = this == context.getString(R.string.key_crypto_settings_sort_value_market_cap)

        val String.isDescending: Boolean
            get() = this == context.getString(R.string.key_crypto_settings_order_value_descending)

        override fun compare(left: Coin, right: Coin): Int {
            if (sort.isMarketCap) {
                val leftCap = left.getQuote(currency)
                val rightCap = right.getQuote(currency)
                if (leftCap != null && rightCap != null) {
                    if (order.isDescending) {
                        return rightCap.getMarketCap().compareTo(leftCap.getMarketCap())
                    } else {
                        return leftCap.getMarketCap().compareTo(rightCap.getMarketCap())
                    }
                }
            }
            return 0
        }
    }


}