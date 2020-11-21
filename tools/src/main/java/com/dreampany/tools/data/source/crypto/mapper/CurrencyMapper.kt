package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.framework.data.model.Time
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.tools.api.crypto.model.cmc.CryptoCurrency
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.data.source.crypto.room.dao.CurrencyDao
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 11/18/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CurrencyMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val timeRepo: TimeRepo,
    private val pref: Prefs,
    private val gson: Gson
) {
    @Transient
    private var cached: Boolean = false
    private val currencies: MutableMap<String, Currency>
    private val key: String

    init {
        currencies = Maps.newConcurrentMap()
        key = StringBuilder(Constants.Keys.Pref.EXPIRE).append(Constants.Keys.Pref.Crypto.CURRENCY)
            .toString()
    }

    @Throws
    @Synchronized
    suspend fun writeExpire(): Long {
        val time = Time(key, Type.CURRENCY.value, Subtype.DEFAULT.value, State.DEFAULT.value)
        return timeRepo.write(time)
    }

    @Throws
    @Synchronized
    suspend fun isExpired(): Boolean {
        val time =
            timeRepo.readTime(key, Type.CURRENCY.value, Subtype.DEFAULT.value, State.DEFAULT.value)
        return time.isExpired(Constants.Times.Crypto.CURRENCY)
    }

    @Throws
    @Synchronized
    suspend fun write(input: Currency) = currencies.put(input.id, input)

    @Throws
    @Synchronized
    suspend fun read(input: CryptoCurrency): Currency {
        var output: Currency? = currencies.get(input.id)
        if (output == null) {
            output = Currency(input.id)
            currencies.put(input.id, output)
        }

        output.name = input.name
        output.sign = input.sign
        output.symbol = input.symbol
        output.type = Currency.Type.FIAT

        return output
    }

    @Throws
    @Synchronized
    suspend fun read(input: Coin): Currency {
        var output: Currency? = currencies.get(input.id)
        if (output == null) {
            output = Currency(input.id)
            currencies.put(input.id, output)
        }

        output.name = input.name
        output.sign = input.symbol
        output.symbol = input.symbol
        output.type = Currency.Type.FIAT

        return output
    }

    @Throws
    @Synchronized
    suspend fun reads(inputs: List<CryptoCurrency>?): List<Currency>? = inputs?.map { read(it) }

    @Throws
    @Synchronized
    suspend fun reads(dao: CurrencyDao): List<Currency>? {
        cache(dao)
        return currencies.values.toList()
    }

    @Throws
    @Synchronized
    private suspend fun cache(dao: CurrencyDao) {
        if (cached) return
        cached = true
        dao.all?.let {
            if (it.isNotEmpty())
                it.forEach { write(it) }
        }
    }
}