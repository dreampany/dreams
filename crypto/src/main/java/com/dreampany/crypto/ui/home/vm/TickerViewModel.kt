package com.dreampany.crypto.ui.home.vm

import android.app.Application
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.Ticker
import com.dreampany.crypto.data.source.repo.TickerRepo
import com.dreampany.crypto.misc.func.CurrencyFormatter
import com.dreampany.crypto.ui.home.model.TickerItem
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TickerViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val formatter: CurrencyFormatter,
    private val repo: TickerRepo
) : BaseViewModel<Type, Subtype, State, Action, Ticker, TickerItem, UiTask<Type, Subtype, State, Action, Ticker>>(
    application,
    rm
) {

    fun loadTickers(id: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Ticker>? = null
            var errors: SmartError? = null
            try {
                result = repo.getTickers(id)
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

    private suspend fun List<Ticker>.toItems(): List<TickerItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                TickerItem.getItem(input, formatter)
            }
        }
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.TICKER,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.TICKER,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<TickerItem>?) {
        postMultiple(
            Type.TICKER,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}