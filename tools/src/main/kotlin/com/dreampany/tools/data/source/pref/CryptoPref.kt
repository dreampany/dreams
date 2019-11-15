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
        return Constants.Pref.Crypto.CRYPTO
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
/*
    fun setStationState(state: State) {
        setPrivately(Constants.Pref.Radio.STATION_STATE, state)
    }

    fun getStationState(defaultState: State): State {
        return getPrivately(Constants.Pref.Radio.STATION_STATE, State::class.java, defaultState)
    }

    fun commitStationTime(state: State) {
        setPrivately(Constants.Pref.Radio.STATION_TIME.plus(state.name), TimeUtilKt.currentMillis())
    }

    fun commitStationTime(state: State, countryCode: String) {
        setPrivately(
            Constants.Pref.Radio.STATION_TIME.plus(state.name).plus(countryCode),
            TimeUtilKt.currentMillis()
        )
    }

    fun getStationTime(state: State): Long {
        return getPrivately(Constants.Pref.Radio.STATION_TIME.plus(state.name), 0L)
    }

    fun getStationTime(state: State, countryCode: String): Long {
        return getPrivately(
            Constants.Pref.Radio.STATION_TIME.plus(state.name).plus(countryCode),
            0L
        )
    }*/
}