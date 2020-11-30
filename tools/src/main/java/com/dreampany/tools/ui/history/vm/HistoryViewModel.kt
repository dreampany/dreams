package com.dreampany.tools.ui.history.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.history.*
import com.dreampany.tools.data.model.history.History
import com.dreampany.tools.data.source.history.pref.Prefs
import com.dreampany.tools.data.source.history.repo.HistoryRepo
import com.dreampany.tools.ui.history.model.HistoryItem
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
class HistoryViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: Prefs,
    private val repo: HistoryRepo
) : BaseViewModel<HistoryType, HistorySubtype, HistoryState, HistoryAction, History, HistoryItem, UiTask<HistoryType, HistorySubtype, HistoryState, HistoryAction, History>>(
    application,
    rm
) {

    fun loadHistories(state: HistoryState, month: Int, day: Int) {
        uiScope.launch {
            postProgress(true)
            var result: List<History>? = null
            var errors: SmartError? = null
            try {
                val source = HistorySource.WIKIPEDIA
                result = repo.gets(source, state, month, day)
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

    /*fun loadStations(state: RadioState, offset: Long) {
        uiScope.launch {
            postProgress(true)
            var result: List<Station>? = null
            var errors: SmartError? = null
            try {
                result = if (state == RadioState.TRENDS) repo.getItemsOfTrends(
                    RadioConstants.Limits.STATIONS
                ) else
                    repo.getItemsOfPopular(
                        RadioConstants.Limits.STATIONS
                    )
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else if (result != null) {
                postResult(result.toItems())
            }
        }
    }*/

    private suspend fun List<History>.toItems(): List<HistoryItem> {
        val list = this
        return withContext(Dispatchers.IO) {
            list.map { HistoryItem(it) }
        }
    }

    private fun postProgress(progress: Boolean) {
        postProgressMultiple(
            HistoryType.HISTORY,
            HistorySubtype.DEFAULT,
            HistoryState.DEFAULT,
            HistoryAction.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            HistoryType.HISTORY,
            HistorySubtype.DEFAULT,
            HistoryState.DEFAULT,
            HistoryAction.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<HistoryItem>?) {
        postMultiple(
            HistoryType.HISTORY,
            HistorySubtype.DEFAULT,
            HistoryState.DEFAULT,
            HistoryAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}