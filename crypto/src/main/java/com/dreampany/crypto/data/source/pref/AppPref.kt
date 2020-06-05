package com.dreampany.crypto.data.source.pref

import android.content.Context
import com.dreampany.crypto.data.enums.Currency
import com.dreampany.crypto.data.enums.Sort
import com.dreampany.crypto.misc.constants.AppConstants
import com.dreampany.framework.data.enums.Order
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.util.Util
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AppPref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String = AppConstants.Keys.Pref.PREF

    @Synchronized
    fun getCurrency(): Currency {
        return getPrivately(
            AppConstants.Keys.Pref.CURRENCY,
            Currency::class.java,
            Currency.USD
        )
    }

    @Synchronized
    fun getSort(): Sort {
        return getPrivately(
            AppConstants.Keys.Pref.SORT,
            Sort::class.java,
            Sort.MARKET_CAP
        )
    }

    @Synchronized
    fun getOrder(): Order {
        return getPrivately(
            AppConstants.Keys.Pref.ORDER,
            Order::class.java,
            Order.DESCENDING
        )
    }

    @Synchronized
    fun getExpireTime(currency: Currency, sort: Sort, order: Order, offset: Long): Long {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(currency.name)
            append(sort.name)
            append(order.name)
            append(offset)
        }
        return getPrivately(key.toString(), Constants.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(currency: Currency, sort: Sort, order: Order, offset: Long) {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(currency.name)
            append(sort.name)
            append(order.name)
            append(offset)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }

    @Synchronized
    fun getExpireTime(id: String, currency: Currency): Long {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(id)
            append(currency.name)
        }
        return getPrivately(key.toString(), Constants.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(id: String, currency: Currency) {
        val key = StringBuilder(AppConstants.Keys.Pref.EXPIRE).apply {
            append(id)
            append(currency.name)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }
}