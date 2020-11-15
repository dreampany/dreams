package com.dreampany.tools.data.source.crypto.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.misc.constants.CryptoConstants
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 11/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Prefs
@Inject constructor(
    context: Context,
    private val gson: Gson
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.Crypto.PREF

    val currency: Currency
        get() = getPublicly(
            context.getString(R.string.key_crypto_settings_currency),
            Currency::class.java,
            null
        ) ?: Currency.USD

    val graphCurrency: Currency
        get() = getPublicly(
            context.getString(R.string.key_crypto_settings_graph_currency),
            Currency::class.java,
            null
        ) ?: Currency.USD

    val sort: String
        get() = getPublicly(
            context.getString(R.string.key_crypto_settings_sort),
            context.getString(R.string.key_crypto_settings_sort_value_market_cap)
        )

    val order: String
        get() = getPublicly(
            context.getString(R.string.key_crypto_settings_order),
            context.getString(R.string.key_crypto_settings_order_value_descending)
        )

    @Synchronized
    fun writeExpireTime(currency: Currency, sort: String, order: String, offset: Long) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(currency.name)
            append(sort)
            append(order)
            append(offset)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun readExpireTime(currency: Currency, sort: String, order: String, offset: Long): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(currency.name)
            append(sort)
            append(order)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun writeExpireTime(currency: Currency, id: String) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(currency.name)
            append(id)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun readExpireTime(currency: Currency, id: String): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(currency.name)
            append(id)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }
}