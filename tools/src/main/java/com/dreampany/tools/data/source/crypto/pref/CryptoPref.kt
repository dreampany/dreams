package com.dreampany.tools.data.source.crypto.pref

import android.content.Context
import com.dreampany.common.data.enums.Order
import com.dreampany.common.data.source.pref.BasePref
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.util.Util
import com.dreampany.tools.data.enums.crypto.CoinSort
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.misc.constant.CryptoConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CryptoPref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return CryptoConstants.Keys.PrefKeys.CRYPTO
    }

    @Synchronized
    fun getCurrency(): Currency {
        return getPrivately(
            CryptoConstants.Keys.PrefKeys.Crypto.CURRENCY,
            Currency::class.java,
            Currency.USD
        )
    }

    @Synchronized
    fun getSort(): CoinSort {
        return getPrivately(
            CryptoConstants.Keys.PrefKeys.Crypto.SORT,
            CoinSort::class.java,
            CoinSort.MARKET_CAP
        )
    }

    @Synchronized
    fun getOrder(): Order {
        return getPrivately(
            CryptoConstants.Keys.PrefKeys.Crypto.ORDER,
            Order::class.java,
            Order.DESCENDING
        )
    }

    @Synchronized
    fun getExpireTime(currency: Currency, sort: CoinSort, order: Order, offset: Long): Long {
        val key = StringBuilder(CryptoConstants.Keys.PrefKeys.Crypto.EXPIRE).apply {
            append(currency.name)
            append(sort.name)
            append(order.name)
            append(offset)
        }
        return getPrivately(key.toString(), Constants.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(currency: Currency, sort: CoinSort, order: Order, offset: Long) {
        val key = StringBuilder(CryptoConstants.Keys.PrefKeys.Crypto.EXPIRE).apply {
            append(currency.name)
            append(sort.name)
            append(order.name)
            append(offset)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }

    @Synchronized
    fun getExpireTime(id: String, currency: Currency): Long {
        val key = StringBuilder(CryptoConstants.Keys.PrefKeys.Crypto.EXPIRE).apply {
            append(id)
            append(currency.name)
        }
        return getPrivately(key.toString(), Constants.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(id: String, currency: Currency) {
        val key = StringBuilder(CryptoConstants.Keys.PrefKeys.Crypto.EXPIRE).apply {
            append(id)
            append(currency.name)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }
}