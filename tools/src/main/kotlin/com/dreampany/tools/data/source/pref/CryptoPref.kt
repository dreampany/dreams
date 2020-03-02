package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 8/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CryptoPref
@Inject constructor(
    context: Context
) : FramePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constants.Pref.NAME.CRYPTO
    }

    fun getCurrency(): Currency {
        return getPrivately(Constants.Pref.Crypto.CURRENCY, Currency::class.java, Currency.USD)
    }

    fun getSort(): CoinSort {
        return getPrivately(Constants.Pref.Crypto.SORT, CoinSort::class.java, CoinSort.MARKET_CAP)
    }

    fun getOrder(): Order {
        return getPrivately(Constants.Pref.Crypto.ORDER, Order::class.java, Order.DESCENDING)
    }

    @Synchronized
    fun getExpireTime(currency: Currency, sort: CoinSort, order: Order, start: Long): Long {
        return getPrivately(Constants.Pref.Crypto.EXPIRE + currency.name + sort.name + order.name + start, 0L)
    }

    @Synchronized
    fun commitExpireTime(currency: Currency, sort: CoinSort, order: Order, start: Long) {
         setPrivately(Constants.Pref.Crypto.EXPIRE + currency.name + sort.name + order.name + start, TimeUtilKt.currentMillis())
    }

    @Synchronized
    fun getExpireTime( currency: Currency, id : String): Long {
        return getPrivately(Constants.Pref.Crypto.EXPIRE + currency.name + id, 0L)
    }

    @Synchronized
    fun commitExpireTime(currency: Currency, id : String) {
        setPrivately(Constants.Pref.Crypto.EXPIRE + currency.name + id, TimeUtilKt.currentMillis())
    }

    @Synchronized
    fun commitExpireTime(currency: Currency, id : String, time: Long) {
        setPrivately(Constants.Pref.Crypto.EXPIRE + currency.name + id, time)
    }
}