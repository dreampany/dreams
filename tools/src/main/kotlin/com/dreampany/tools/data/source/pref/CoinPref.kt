package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.framework.util.TimeUtilKt
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
class CoinPref
@Inject constructor(
    context: Context
) : FramePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constants.Pref.Coin.COIN
    }

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
        setPrivately(Constants.Pref.Radio.STATION_TIME.plus(state.name).plus(countryCode), TimeUtilKt.currentMillis())
    }

    fun getStationTime(state: State): Long {
        return getPrivately(Constants.Pref.Radio.STATION_TIME.plus(state.name), 0L)
    }

    fun getStationTime(state: State, countryCode: String): Long {
        return getPrivately(Constants.Pref.Radio.STATION_TIME.plus(state.name).plus(countryCode), 0L)
    }
}