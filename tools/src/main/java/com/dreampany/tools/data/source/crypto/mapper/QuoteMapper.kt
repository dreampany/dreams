package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.framework.data.model.Time
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.framework.misc.exts.utc
import com.dreampany.tools.api.crypto.model.cmc.CryptoQuote
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.room.dao.QuoteDao
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 11/20/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class QuoteMapper
@Inject constructor(
    private val timeRepo: TimeRepo
) {
    @Transient
    private var cached: Boolean = false
    private val quotes: MutableMap<String, Quote> // key will be id plus currency id

    init {
        quotes = Maps.newConcurrentMap()
    }

    @Throws
    @Synchronized
    suspend fun writeExpire(id: String, currency: Currency): Long {
        val time =
            Time(id.plus(currency.id), Type.QUOTE.value, Subtype.DEFAULT.value, State.DEFAULT.value)
        return timeRepo.write(time)
    }

    @Throws
    @Synchronized
    suspend fun isExpired(id: String, currency: Currency): Boolean {
        val time =
            timeRepo.readTime(
                id.plus(currency.id),
                Type.QUOTE.value,
                Subtype.DEFAULT.value,
                State.DEFAULT.value
            )
        return time.isExpired(Constants.Times.Crypto.QUOTE)
    }

    @Throws
    @Synchronized
    fun write(input: Quote) = quotes.put(input.id.plus(input.getCurrencyId()), input)

    @Throws
    @Synchronized
    fun read(id: String, currency: Currency, input: CryptoQuote): Quote {
        val key = id.plus(currency.id)
        var output: Quote? = quotes.get(key)
        if (output == null) {
            output = Quote(id)
            quotes.put(key, output)
        }

        output.setCurrencyId(currency.id)
        output.price = input.price
        output.setVolume24h(input.volume24h)
        output.setVolume24hReported(input.volume24hReported)
        output.setVolume7d(input.volume7d)
        output.setVolume7dReported(input.volume7dReported)
        output.setVolume30d(input.volume30d)
        output.setVolume30dReported(input.volume30dReported)
        output.setMarketCap(input.marketCap)
        output.setPercentChange1h(input.percentChange1h)
        output.setPercentChange24h(input.percentChange24h)
        output.setPercentChange7d(input.percentChange7d)
        output.setLastUpdated(input.lastUpdated.utc(Constants.Pattern.Crypto.CMC_DATE_TIME))

        return output
    }

    @Throws
    @Synchronized
    suspend fun read(id: String, currency: Currency, dao: QuoteDao): Quote? {
        update(id, currency, dao)
        val key = id.plus(currency.id)
        return quotes.get(key)
    }

    @Throws
    @Synchronized
    private suspend fun update(id: String, currency: Currency, dao: QuoteDao) {
        val key = id.plus(currency.id)
        if (!quotes.containsKey(key)) {
            val quote = dao.read(id, currency.id)
            quote?.let { write(it) }
        }
    }

    @Throws
    @Synchronized
    private fun cache(dao: QuoteDao) {
        if (cached) return
        cached = true
        dao.all?.let {
            if (it.isNotEmpty())
                it.forEach { write(it) }
        }
    }
}