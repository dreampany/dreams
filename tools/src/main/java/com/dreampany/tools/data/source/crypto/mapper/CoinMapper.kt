package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.common.data.enums.Order
import com.dreampany.common.misc.extension.isExpired
import com.dreampany.common.misc.extension.sub
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao
import com.dreampany.tools.misc.comparator.Comparators
import com.dreampany.tools.misc.constant.AppConstants
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
    private val coins: MutableList<Coin>

    init {
        coins = Collections.synchronizedList(ArrayList<Coin>())
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
        if (!coins.contains(coin)) {
            coins.add(coin)
        }
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
        val comparator = Comparators.Crypto.getComparator(currency, sort, sortDirection)
        coins.sortWith(comparator)
        return coins
    }


}