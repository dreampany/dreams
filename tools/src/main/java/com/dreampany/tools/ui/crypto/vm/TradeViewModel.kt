package com.dreampany.tools.ui.crypto.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.model.crypto.Trade
import com.dreampany.tools.data.source.crypto.repo.TradeRepo
import com.dreampany.tools.misc.constant.CryptoConstants
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
) : BaseViewModel<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Trade, Trade, UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Trade>>(
    application,
    rm
) {

    fun loadTrades(fromSymbol: String, extraParams: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Trade>? = null
            var errors: SmartError? = null
            try {
                result = repo.getTrades(fromSymbol, extraParams, CryptoConstants.Limits.TRADES)
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
            CryptoType.TRADE,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            progress = progress
        )
    }

    private fun postError(error: SmartError) {
        postMultiple(
            CryptoType.TRADE,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<Trade>?) {
        postMultiple(
            CryptoType.TRADE,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}