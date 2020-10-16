package com.dreampany.crypto.ui.home.vm

import android.app.Application
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.Exchange
import com.dreampany.crypto.data.source.repo.ExchangeRepo
import com.dreampany.crypto.misc.constants.Constants
import com.dreampany.crypto.misc.func.CurrencyFormatter
import com.dreampany.crypto.ui.home.model.ExchangeItem
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
class ExchangeViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val formatter: CurrencyFormatter,
    private val repo: ExchangeRepo
) : BaseViewModel<Type, Subtype, State, Action, Exchange, ExchangeItem, UiTask<Type, Subtype, State, Action, Exchange>>(
    application,
    rm
) {

    fun loadExchanges(fromSymbol: String, toSymbol: String, extraParams: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Exchange>? = null
            var errors: SmartError? = null
            try {
                result = repo.getExchanges(
                    fromSymbol,
                    toSymbol,
                    extraParams,
                    Constants.Limits.EXCHANGES
                )
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
            Type.EXCHANGE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.EXCHANGE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<ExchangeItem>?) {
        postMultiple(
            Type.EXCHANGE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}