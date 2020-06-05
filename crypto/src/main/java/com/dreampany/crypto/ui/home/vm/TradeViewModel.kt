package com.dreampany.crypto.ui.home.vm

import android.app.Application
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.Trade
import com.dreampany.crypto.data.source.repo.TradeRepo
import com.dreampany.crypto.misc.constants.AppConstants
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TradeViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val repo: TradeRepo
) : BaseViewModel<Type, Subtype, State, Action, Trade, Trade, UiTask<Type, Subtype, State, Action, Trade>>(
    application,
    rm
) {

    fun loadTrades(fromSymbol: String, extraParams: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Trade>? = null
            var errors: SmartError? = null
            try {
                result = repo.getTrades(fromSymbol, extraParams, AppConstants.Limits.TRADES)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result)
            }
        }
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.TRADE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postError(error: SmartError) {
        postMultiple(
            Type.TRADE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<Trade>?) {
        postMultiple(
            Type.TRADE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}