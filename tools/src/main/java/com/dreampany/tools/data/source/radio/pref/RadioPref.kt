package com.dreampany.tools.data.source.radio.pref

import android.content.Context
import com.dreampany.common.data.source.pref.BasePref
import com.dreampany.common.misc.util.Util
import com.dreampany.tools.data.enums.RadioState
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

    fun setStationState(state: RadioState) {
        setPrivately(RadioConstants.Keys.Radio.STATION_STATE, state)
    }

    fun getStationState(defaultState: RadioState): RadioState {
        return getPrivately(RadioConstants.Keys.Radio.STATION_STATE, RadioState::class.java, defaultState)
    }

    fun commitStationTime(state: RadioState) {
        setPrivately(RadioConstants.Keys.Radio.STATION_TIME.plus(state.name), Util.currentMillis())
    }

    fun commitStationTime(state: RadioState, countryCode: String) {
        setPrivately(RadioConstants.Keys.Radio.STATION_TIME.plus(state.name).plus(countryCode), Util.currentMillis())
    }

    fun getStationTime(state: RadioState): Long {
        return getPrivately(RadioConstants.Keys.Radio.STATION_TIME.plus(state.name), 0L)
    }

    fun getStationTime(state: RadioState, countryCode: String): Long {
        return getPrivately(RadioConstants.Keys.Radio.STATION_TIME.plus(state.name).plus(countryCode), 0L)
    }
}