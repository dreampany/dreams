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
import com.dreampany.tools.data.model.crypto.Ticker
import com.dreampany.tools.data.source.crypto.repo.TickerRepo
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.ui.crypto.model.TickerItem
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
) : BaseViewModel<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Ticker, TickerItem, UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Ticker>>(
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
            CryptoType.TICKER,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            CryptoType.TICKER,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<TickerItem>?) {
        postMultiple(
            CryptoType.TICKER,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}