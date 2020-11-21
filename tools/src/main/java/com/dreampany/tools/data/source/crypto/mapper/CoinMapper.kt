package com.dreampany.tools.data.source.crypto.mapper

import android.content.Context
import com.dreampany.framework.data.model.Time
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.framework.misc.exts.sub
import com.dreampany.framework.misc.exts.utc
import com.dreampany.framework.misc.exts.value
import com.dreampany.tools.R
import com.dreampany.tools.api.crypto.model.cmc.CryptoCoin
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.misc.constants.CryptoConstants
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
class CoinMapper
@Inject constructor(
    private val context: Context,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val timeRepo: TimeRepo,
    private val pref: Prefs
) {
    @Transient
    private var cached: Boolean = false
    private val coins: MutableMap<String, Coin>
    private val favorites: MutableMap<String, Boolean>

    init {
        coins = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    @Throws
    @Synchronized
    suspend fun writeExpire(currency: Currency, sort: String, order: String, offset: Long): Long {
        val key =
            StringBuilder(Constants.Keys.Pref.EXPIRE).append(currency.id).append(sort).append(order)
                .append(offset)
        val time = Time(key.toString(), Type.COIN.value, Subtype.DEFAULT.value, State.DEFAULT.value)
        return timeRepo.write(time)
    }

    @Throws
    @Synchronized
    suspend fun isExpired(currency: Currency, sort: String, order: String, offset: Long): Boolean {
        val key =
            StringBuilder(Constants.Keys.Pref.EXPIRE).append(currency.id).append(sort).append(order)
                .append(offset)
        val time =
            timeRepo.readTime(
                key.toString(),
                Type.COIN.value,
                Subtype.DEFAULT.value,
                State.DEFAULT.value
            )
        return time.isExpired(Constants.Times.Crypto.COINS)
    }

    @Throws
    @Synchronized
    suspend fun writeExpire(id: String, currency: Currency): Long {
        val key =
            StringBuilder(Constants.Keys.Pref.EXPIRE).append(id).append(currency.id)
        val time = Time(key.toString(), Type.COIN.value, Subtype.DEFAULT.value, State.DEFAULT.value)
        return timeRepo.write(time)
    }

    @Throws
    @Synchronized
    suspend fun isExpired(id: String, currency: Currency): Boolean {
        val key =
            StringBuilder(Constants.Keys.Pref.EXPIRE).append(id).append(currency.id)
        val time =
            timeRepo.readTime(
                key.toString(),
                Type.COIN.value,
                Subtype.DEFAULT.value,
                State.DEFAULT.value
            )
        return time.isExpired(CryptoConstants.Times.Crypto.COIN)
    }

    @Throws
    @Synchronized
    suspend fun write(input: Coin) = coins.put(input.id, input)

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
    suspend fun reads(
        currency: Currency,
        sort: String,
        order: String,
        offset: Long,
        limit: Long,
        quoteDao: QuoteDao,
        source: CoinDataSource
    ): List<Coin>? {
       // updateCache(source)
        val cache = sortedCoins(currency, coins.values.toList(), sort, order)
        val result = sub(cache, offset, limit)
        result?.forEach {
            //bindQuote(currency, it, quoteDao)
        }
        return result
    }

    @Throws
    @Synchronized
    suspend fun read(
        currency: Currency,
        id: String,
        quoteDao: QuoteDao,
        source: CoinDataSource
    ): Coin? {
        //updateCache(source)
        val result = coins.get(id)
        result?.let {
            //bindQuote(currency, it, quoteDao)
        }
        return result
    }

    @Throws
    @Synchronized
    suspend fun readFavorites(
        currency: Currency,
        sort: String,
        order: String,
        source: CoinDataSource
    ): List<Coin>? {
        //updateCache(source)
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
/*        result?.forEach {
            bindQuote(currency, it, quoteDao)
        }*/
        return result
    }

    @Throws
    @Synchronized
    suspend fun read(inputs: List<CryptoCoin>): List<Coin> = inputs.map { read(it) }

    @Throws
    @Synchronized
    suspend fun read(input: CryptoCoin): Coin {
        Timber.v("Resolved Coin: %s", input.name);
        val id = input.id
        var output: Coin? = coins.get(id)
        if (output == null) {
            output = Coin(id)
            coins.put(id, output)
        }
        output.name = input.name
        output.symbol = input.symbol
        output.slug = input.slug

        output.rank = input.rank
        output.setMarketPairs(input.marketPairs)

        output.setCirculatingSupply(input.circulatingSupply)
        output.setTotalSupply(input.totalSupply)
        output.setMarketCap(input.marketCap)
        output.setMaxSupply(input.maxSupply)

        output.setLastUpdated(input.lastUpdated.utc(Constants.Pattern.Crypto.CMC_DATE_TIME))
        output.setDateAdded(input.dateAdded.utc(Constants.Pattern.Crypto.CMC_DATE_TIME))

        output.tags = input.tags
        return output
    }

   /* @Synchronized
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
    }*/

    /*@Synchronized
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
    }*/

/*    @Synchronized
    private fun bindQuote(currency: Currency, item: Coin, dao: QuoteDao) {
        if (!item.hasQuote(currency)) {
            dao.read(item.id, currency.name)?.let { item.addQuote(it) }
        }
    }*/

/*    @Throws
    @Synchronized
    private suspend fun cache(dao: CurrencyDao) {
        if (cached) return
        cached = true
        dao.all?.let {
            if (it.isNotEmpty())
                it.forEach { write(it) }
        }
    }*/

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
                /*val leftCap = left.getQuote(currency)
                val rightCap = right.getQuote(currency)
                if (leftCap != null && rightCap != null) {
                    if (order.isDescending) {
                        return rightCap.getMarketCap().compareTo(leftCap.getMarketCap())
                    } else {
                        return leftCap.getMarketCap().compareTo(rightCap.getMarketCap())
                    }
                }*/
            }
            return 0
        }
    }


}