package com.dreampany.tools.ui.radio.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.radio.Page
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.data.source.radio.pref.Prefs
import com.dreampany.tools.data.source.radio.repo.StationRepo
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.ui.radio.model.StationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
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
                    repo.readsByCountryCode(countryCode, order, offset, Constants.Limits.STATIONS)
                if (result.isNullOrEmpty()) {
                    result = repo.readsByCountryCode(
                        Locale.US.country,
                        order,
                        offset,
                        Constants.Limits.STATIONS
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
                        result = repo.readsTrend(order, offset, Constants.Limits.STATIONS)
                    }
                    Page.Type.POPULAR -> {
                        result = repo.readsPopular(order, offset, Constants.Limits.STATIONS)
                    }
                    Page.Type.RECENT -> {
                        result = repo.readsRecent(order, offset, Constants.Limits.STATIONS)
                    }
                    Page.Type.CHANGE -> {
                        result = repo.readsChange(order, offset, Constants.Limits.STATIONS)
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
                result = repo.searchByName(query, order, offset, Constants.Limits.STATIONS)
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