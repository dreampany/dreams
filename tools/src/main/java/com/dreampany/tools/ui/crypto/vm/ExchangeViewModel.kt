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
import com.dreampany.tools.data.model.crypto.Exchange
import com.dreampany.tools.data.source.crypto.repo.ExchangeRepo
import com.dreampany.tools.misc.constants.CryptoConstants
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.ui.crypto.model.ExchangeItem
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
class ExchangeViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val formatter: CurrencyFormatter,
    private val repo: ExchangeRepo
) : BaseViewModel<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Exchange, ExchangeItem, UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Exchange>>(
    application,
    rm
) {

    fun loadExchanges(fromSymbol: String, toSymbol: String, extraParams: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Exchange>? = null
            var errors: SmartError? = null
            try {
                result = repo.getExchanges(fromSymbol, toSymbol, extraParams, CryptoConstants.Limits.EXCHANGES)
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

    private suspend fun List<Exchange>.toItems(): List<ExchangeItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                ExchangeItem.getItem(input, formatter)
            }
        }
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            CryptoType.EXCHANGE,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            CryptoType.EXCHANGE,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<ExchangeItem>?) {
        postMultiple(
            CryptoType.EXCHANGE,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}