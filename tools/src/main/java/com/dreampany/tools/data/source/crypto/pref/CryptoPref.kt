package com.dreampany.tools.data.source.crypto.pref

import android.content.Context
import com.dreampany.common.data.enums.Order
import com.dreampany.common.data.source.pref.BasePref
import com.dreampany.common.misc.util.Util
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.misc.constant.AppConstants
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
        return AppConstants.Keys.PrefKeys.CRYPTO
    }

    @Synchronized
    fun getCurrency(): Currency {
        return getPrivately(AppConstants.Keys.PrefKeys.Crypto.CURRENCY, Currency::class.java, Currency.USD) ?: Currency.USD
    }

    @Synchronized
    fun getSort(): CoinSort {
        return getPrivately(AppConstants.Keys.PrefKeys.Crypto.SORT, CoinSort::class.java, CoinSort.MARKET_CAP) ?: CoinSort.MARKET_CAP
    }

    @Synchronized
    fun getOrder(): Order {
        return getPrivately(AppConstants.Keys.PrefKeys.Crypto.ORDER, Order::class.java, Order.DESCENDING) ?: Order.DESCENDING
    }

    @Synchronized
    fun getExpireTime(currency: Currency, sort: CoinSort, order: Order, start: Long): Long {
        return getPrivately(AppConstants.Keys.PrefKeys.Crypto.EXPIRE + currency.name + sort.name + order.name + start, 0L)
    }

    @Synchronized
    fun commitExpireTime(currency: Currency, sort: CoinSort, order: Order, start: Long) {
        setPrivately(AppConstants.Keys.PrefKeys.Crypto.EXPIRE + currency.name + sort.name + order.name + start, Util.currentMillis())
    }
}