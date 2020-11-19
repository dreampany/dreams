package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.exts.isExpired
import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.data.source.crypto.pref.Prefs
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

    private val currencies: MutableMap<String, MutableMap<String, Currency>>

    init {
        currencies = Maps.newConcurrentMap()
    }


    fun writeExpire() = pref.writeExpireTimeOfCurrency()

    val isExpired: Boolean
        get() {
            val time = pref.readExpireTimeOfCurrency()
            return time.isExpired(Constants.Times.Crypto.CURRENCY)
        }
}