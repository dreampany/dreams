package com.dreampany.tools.data.source.radio.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.misc.constants.RadioConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 15/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class RadioPref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return RadioConstants.Keys.PrefKeys.RADIO
    }

    @Synchronized
    fun getStationOrder(): StationOrder {
        return getPrivately(
            RadioConstants.Keys.PrefKeys.Station.ORDER,
            StationOrder::class.java,
            StationOrder.NAME
        )
    }

    @Synchronized
    fun setStationState(state: RadioState) {
        setPrivately(RadioConstants.Keys.Radio.STATION_STATE, state)
    }

    @Synchronized
    fun getStationState(defaultState: RadioState): RadioState {
        return getPrivately(
            RadioConstants.Keys.Radio.STATION_STATE,
            RadioState::class.java,
            defaultState
        )
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
        state: RadioState, countryCode: String, hideBroken: Boolean, order: StationOrder, reverse: Boolean, offset: Long
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