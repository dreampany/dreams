package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.framework.data.model.Time
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.framework.misc.exts.utc
import com.dreampany.tools.api.crypto.model.cmc.CryptoQuote
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import com.google.gson.Gson
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
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val timeRepo: TimeRepo,
    private val pref: Prefs,
    private val gson: Gson
) {

    private val quotes: MutableMap<String, Quote> // key will be id plus currency id

    init {
        quotes = Maps.newConcurrentMap()
    }

    @Throws
    suspend fun writeExpire(id: String, currency: Currency) {
        val time = Time(id, Type.QUOTE.value, Subtype.DEFAULT.value, State.DEFAULT.value)
        timeRepo.write(time)
    }

    @Throws
    suspend fun isExpired(id: String): Boolean {
        val time =
            timeRepo.readTime(id, Type.QUOTE.value, Subtype.DEFAULT.value, State.DEFAULT.value)
        return time.isExpired(Constants.Times.Crypto.QUOTE)
    }

    fun write(input: Quote) = quotes.put(input.id.plus(input.getCurrencyId()), input)

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
        output.setPercentChange1h(input.marketCap)
        output.setPercentChange24h(input.marketCap)
        output.setPercentChange7d(input.marketCap)
        output.setLastUpdated(input.lastUpdated.utc(Constants.Pattern.Crypto.CMC_DATE_TIME))

        return output
    }
}