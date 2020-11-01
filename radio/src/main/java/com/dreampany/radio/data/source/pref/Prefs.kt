package com.dreampany.radio.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.misc.Constants
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/11/20
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

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.PREF

    val stationOrder: Station.Order
        get() = getPrivately(
            Constants.Keys.Pref.ORDER,
            Station.Order::class.java,
            null
        ) ?: Station.Order.NAME

    fun write(state: RadioState) {
        setPrivately(RadioConstants.Keys.Radio.STATION_STATE, state)
    }

    @Synchronized
    fun getStationState(defaultState: RadioState): RadioState {
        return getPrivately(
            RadioConstants.Keys.Radio.STATION_STATE,
            RadioState::class.java,
            null
        ) ?: defaultState
    }

    @Synchronized
    fun commitExpireTime(state: RadioState) {
        val key = StringBuilder(RadioConstants.Keys.Radio.STATION_TIME).apply {
            append(state.value)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }

    @Synchronized
    fun commitExpireTime(state: RadioState, order: StationOrder, reverse: Boolean, offset: Long) {
        val key = StringBuilder(RadioConstants.Keys.Radio.STATION_TIME).apply {
            append(state.value)
            append(order.value)
            append(reverse)
            append(offset)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }

    @Synchronized
    fun commitExpireTime(
        state: RadioState,
        countryCode: String,
        hideBroken: Boolean,
        order: StationOrder,
        reverse: Boolean,
        offset: Long
    ) {
        val key = StringBuilder(RadioConstants.Keys.Radio.STATION_TIME).apply {
            append(state.value)
            append(countryCode)
            append(hideBroken)
            append(order.value)
            append(reverse)
            append(offset)
        }
        setPrivately(key.toString(), Util.currentMillis())
    }

    @Synchronized
    fun getExpireTime(
        state: RadioState
    ): Long {
        val key = StringBuilder(RadioConstants.Keys.Radio.STATION_TIME).apply {
            append(state.value)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun getExpireTime(
        state: RadioState,
        order: StationOrder,
        reverse: Boolean,
        offset: Long
    ): Long {
        val key = StringBuilder(RadioConstants.Keys.Radio.STATION_TIME).apply {
            append(state.value)
            append(order.value)
            append(reverse)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun getExpireTime(
        state: RadioState,
        countryCode: String,
        hideBroken: Boolean,
        order: StationOrder,
        reverse: Boolean,
        offset: Long
    ): Long {
        val key = StringBuilder(RadioConstants.Keys.Radio.STATION_TIME).apply {
            append(state.value)
            append(countryCode)
            append(hideBroken)
            append(order.value)
            append(reverse)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }
}