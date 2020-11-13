package com.dreampany.tools.ui.crypto.vm

import android.app.Application
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.framework.misc.exts.isEmpty
import com.dreampany.framework.misc.exts.second
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.data.enums.crypto.Times
import com.dreampany.tools.data.enums.crypto.Times.Companion.startTime
import com.dreampany.tools.data.model.crypto.Graph
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.data.source.crypto.repo.GraphRepo
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 12/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class GraphViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: Prefs,
    private val repo: GraphRepo
) : BaseViewModel<Type, Subtype, State, Action, Graph, Graph, UiTask<Type, Subtype, State, Action, Graph>>(
    application,
    rm
) {

    fun read(currency: Currency, slug: String, time: Times) {
        uiScope.launch {
            postProgress(true)
            var result: Graph? = null
            var errors: SmartError? = null
            try {
                val start = time.startTime
                val end = currentMillis
                result = repo.read(slug, start, end)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(currency))
            }
        }
    }

    private suspend fun Graph.toItem(currency: Currency): Graph {
        val input = this
        return withContext(Dispatchers.IO) {
            val prices = input.prices(currency)
            prices?.let {
                val currentPrice = it.currentPrice
            }
            input
        }
    }

    private fun postProgress(progress: Boolean) {
        postProgressSingle(
            Type.GRAPH,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postError(error: SmartError) {
        postSingle(
            Type.GRAPH,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: Graph?, state: State = State.DEFAULT) {
        postSingle(
            Type.GRAPH,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun Graph.prices(currency: Currency): List<Entry>? {
        var prices = this.priceUsd
        if (currency == Currency.BTC)
            prices = this.priceBtc
        return prices?.map { Entry(it.first(), it.second()) }
    }

    private val List<Entry>.currentTime: Long
        get() = last().x.toLong()

    private val List<Entry>.startPrice: Float
        get() = find{it.y.isEmpty.not()}

    private val List<Entry>.currentPrice: Float
        get() = last().y

    private val List<Entry>.differenceOfPrice: Float
        get() {
            find { it.y.isEmpty.not()}?.y
        }
}