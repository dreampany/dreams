package com.dreampany.tools.ui.crypto.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.data.source.crypto.repo.CoinRepo
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.ui.crypto.model.CoinItem
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
class CoinViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val formatter: CurrencyFormatter,
    private val pref: Prefs,
    private val repo: CoinRepo
) : BaseViewModel<Type, Subtype, State, Action, Coin, CoinItem, UiTask<Type, Subtype, State, Action, Coin>>(
    application,
    rm
) {

    fun loadCoins(offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Pair<Coin, Quote>>? = null
            var errors: SmartError? = null
            try {
                val currency = pref.currency
                val sort = pref.sort
                val order = pref.order
                result = repo.reads(currency, sort, order, offset, Constants.Limits.COINS)
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

    fun loadCoin(id: String) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Pair<Coin, Quote>? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                val currency = pref.currency
                result = repo.read(id, currency)
                result?.let { favorite = repo.isFavorite(it.first) }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite))
            }
        }
    }

    fun loadFavoriteCoins() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Pair<Coin, Quote>>? = null
            var errors: SmartError? = null
            try {
                val currency = pref.currency
                val sort = pref.sort
                val order = pref.order
                result = repo.favorites(currency, sort, order)
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

    fun toggleFavorite(input: Pair<Coin, Quote>) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Pair<Coin, Quote>? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                favorite = repo.toggleFavorite(input.first)
                result = input
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite), state = State.FAVORITE)
            }
        }
    }

    private suspend fun List<Pair<Coin, Quote>>.toItems(): List<CoinItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.currency
            val sort = pref.sort
            val order = pref.order
            input.map { input ->
                val favorite = repo.isFavorite(input.first)
                CoinItem(input, currency, formatter, favorite)
            }
        }
    }

    private suspend fun Pair<Coin, Quote>.toItem(favorite: Boolean): CoinItem {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.currency
            val sort = pref.sort
            val order = pref.order
            CoinItem(input, currency, formatter, favorite = favorite)
        }
    }

    private fun postProgressSingle(progress: Boolean) {
        postProgressSingle(
            Type.COIN,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.COIN,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.COIN,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<CoinItem>?) {
        postMultiple(
            Type.COIN,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun postResult(result: CoinItem?, state: State = State.DEFAULT) {
        postSingle(
            Type.COIN,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}