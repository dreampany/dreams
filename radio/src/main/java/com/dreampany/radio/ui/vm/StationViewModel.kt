package com.dreampany.radio.ui.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.radio.data.enums.Type
import com.dreampany.radio.data.enums.Subtype
import com.dreampany.radio.data.enums.Action
import com.dreampany.radio.data.enums.State
import com.dreampany.radio.data.model.Page
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.data.source.pref.Prefs
import com.dreampany.radio.data.source.repo.StationRepo
import com.dreampany.radio.misc.Constants
import com.dreampany.radio.ui.model.StationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 2/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: Prefs,
    private val repo: StationRepo
) : BaseViewModel<Type, Subtype, State, Action, Station, StationItem, UiTask<Type, Subtype, State, Action, Station>>(
    application,
    rm
) {

    fun readsLocal(countryCode: String, order: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Station>? = null
            var errors: SmartError? = null
            try {
                result =
                    repo.readsByCountryCode(countryCode, order, offset, Constants.Limit.STATIONS)
                if (result.isNullOrEmpty()) {
                    result = repo.readsByCountryCode(
                        Locale.US.country,
                        order,
                        offset,
                        Constants.Limit.STATIONS
                    )
                }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun reads(type: Page.Type, order: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Station>? = null
            var errors: SmartError? = null
            try {
                when (type) {
                    Page.Type.TREND -> {
                        result = repo.readsTrend(order, offset, Constants.Limit.STATIONS)
                    }
                    Page.Type.POPULAR -> {
                        result = repo.readsPopular(order, offset, Constants.Limit.STATIONS)
                    }
                    Page.Type.RECENT -> {
                        result = repo.readsRecent(order, offset, Constants.Limit.STATIONS)
                    }
                    Page.Type.CHANGE -> {
                        result = repo.readsChange(order, offset, Constants.Limit.STATIONS)
                    }
                }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun search(query: String, order: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Station>? = null
            var errors: SmartError? = null
            try {
                result = repo.searchByName(query, order, offset, Constants.Limit.STATIONS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    private suspend fun List<Station>.toItems(): List<StationItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                StationItem(input)
            }
        }
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.STATION,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.STATION,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<StationItem>?) {
        postMultiple(
            Type.STATION,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}