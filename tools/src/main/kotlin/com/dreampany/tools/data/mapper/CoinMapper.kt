package com.dreampany.tools.data.mapper

import android.content.Context
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.model.Quote
import com.dreampany.tools.injector.annotation.NoteAnnote
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

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
    @NoteAnnote private val map: SmartMap<String, Coin>,
    @NoteAnnote private val cache: SmartCache<String, Coin>
) {

    fun getItems(inputs: List<com.dreampany.tools.api.crypto.model.Coin>?): List<Coin>? {
        if (inputs.isNullOrEmpty()) return null
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
        out.quotes = getQuotes(input.quotes)


        return out
    }

    fun getQuotes(input: Map<com.dreampany.tools.api.crypto.model.Currency, com.dreampany.tools.api.crypto.model.Quote>): HashMap<Currency, Quote> {
        val result = Maps.newHashMap<Currency, Quote>()
        input.forEach { entry ->
            result.put()
        }
        return result
    }

    fun getCurrency(input: com.dreampany.tools.api.crypto.model.Currency) : Currency {
        return Currency.valueOf(input.name)
    }

    fun getQuote(input: com.dreampany.tools.api.crypto.model.Quote) : Quote {

    }
}