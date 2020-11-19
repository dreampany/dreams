package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.tools.api.crypto.model.cmc.CryptoCurrency
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.source.crypto.api.CoinDataSource
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

    private val currencies: MutableMap<String, Currency>

    init {
        currencies = Maps.newConcurrentMap()
    }


    fun writeExpire() = pref.writeExpireTimeOfCurrency()

    val isExpired: Boolean
        get() {
            val time = pref.readExpireTimeOfCurrency()
            return time.isExpired(Constants.Times.Crypto.CURRENCY)
        }

    fun write(input: Currency) = currencies.put(input.id, input)

    @Synchronized
    fun read(input: CryptoCurrency): Currency {
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

    @Synchronized
    fun read(input: Coin): Currency {
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

    @Synchronized
    fun reads(inputs: List<CryptoCurrency>?): List<Currency>? = inputs?.map { read(it) }

    @Synchronized
    suspend fun reads(dao : CurrencyDao): List<Currency>? {
        updateCache(dao)
        return currencies.values.toList()
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(dao : CurrencyDao) {
        if (currencies.isEmpty()) {
            dao.all?.let {
                if (it.isNotEmpty())
                    it.forEach { write(it) }
            }
        }
    }
}