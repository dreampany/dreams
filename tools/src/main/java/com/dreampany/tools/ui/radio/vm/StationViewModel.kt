package com.dreampany.tools.ui.radio.vm

import android.app.Application
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.home.Subtype
import com.dreampany.tools.data.enums.home.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.data.source.radio.pref.RadioPref
import com.dreampany.tools.data.source.radio.repo.StationRepo
import com.dreampany.tools.misc.constant.RadioConstants
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.dreampany.tools.ui.radio.model.StationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: RadioPref,
    private val repo: StationRepo
) : BaseViewModel<Type, Subtype, State, Action, Station, StationItem, UiTask<Type, Subtype, State, Action, Station>>(
    application,
    rm
) {

    fun loadStations(countryCode: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(
                Type.STATION,
                Subtype.DEFAULT,
                State.DEFAULT,
                Action.DEFAULT,
                progress = true
            )
            var result: List<Station>? = null
            var errors: Throwable? = null
            try {
                val order = pref.getStationOrder()
                result = repo.getItemsOfCountry(
                    countryCode,
                    true,
                    order,
                    false,
                    offset,
                    RadioConstants.Limits.STATIONS
                )
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postMultiple(
                    Type.STATION,
                    Subtype.DEFAULT,
                    State.DEFAULT,
                    Action.DEFAULT,
                    error = errors,
                    showProgress = true
                )
            } else if (result != null) {
                postMultiple(
                    Type.STATION,
                    Subtype.DEFAULT,
                    State.DEFAULT,
                    Action.DEFAULT,
                    result = result.toItems(),
                    showProgress = true
                )
            }
        }
    }

    suspend fun List<Station>.toItems(): List<StationItem> {
        val list = this
        return withContext(Dispatchers.IO) {
            val order = pref.getStationOrder()
            list.map { StationItem(it, order) }
        }
    }
}