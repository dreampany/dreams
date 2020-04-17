package com.dreampany.tools.data.source.radio.pref

import android.content.Context
import com.dreampany.common.data.source.pref.BasePref
import com.dreampany.common.misc.util.Util
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.misc.constant.RadioConstants
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
        ) ?: StationOrder.NAME
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
        setPrivately(RadioConstants.Keys.Radio.STATION_TIME.plus(state.name), Util.currentMillis())
    }

    @Synchronized
    fun commitExpireTime(state: RadioState, countryCode: String) {
        setPrivately(
            RadioConstants.Keys.Radio.STATION_TIME.plus(state.name).plus(countryCode),
            Util.currentMillis()
        )
    }

    @Synchronized
    fun getExpireTime(state: RadioState): Long {
        return getPrivately(RadioConstants.Keys.Radio.STATION_TIME.plus(state.name), 0L)
    }

    @Synchronized
    fun getExpireTime(state: RadioState, countryCode: String): Long {
        return getPrivately(
            RadioConstants.Keys.Radio.STATION_TIME.plus(state.name).plus(countryCode), 0L
        )
    }
}