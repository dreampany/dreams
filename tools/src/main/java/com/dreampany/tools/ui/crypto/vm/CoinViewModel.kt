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
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.data.source.crypto.repo.CoinRepo
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.misc.constants.CryptoConstants
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
            var result: List<Coin>? = null
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
            var result: Coin? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                val currency = pref.currency
                result = repo.read(currency, id)
                result?.let { favorite = repo.isFavorite(it) }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems(favorite))
            }
        }
    }

    fun loadFavoriteCoins() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Coin>? = null
            var errors: SmartError? = null
            try {
                val currency = pref.currency
                val sort = pref.sort
                val order = pref.order
                result = repo.readsFavorite(currency, sort, order)
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

    fun toggleFavorite(input: Coin, itemType: CoinItem.ItemType) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Coin? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                favorite = repo.toggleFavorite(input)
                result = input
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(itemType, favorite), state = State.FAVORITE)
            }
        }
    }

    private suspend fun List<Coin>.toItems(): List<CoinItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.currency
            val sort = pref.sort
            val order = pref.order
            input.map { input ->
                val favorite = repo.isFavorite(input)
                CoinItem.getItem(input, formatter, currency, sort, order, favorite)
            }
        }
    }

    private suspend fun Coin.toItems(favorite: Boolean): List<CoinItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.currency
            val sort = pref.sort
            val order = pref.order
            val result = arrayListOf<CoinItem>()
            result.add(CoinItem.getInfoItem(input, formatter, currency, sort, order, favorite))
            result.add(CoinItem.getQuoteItem(input, formatter, currency, sort, order, favorite))
            result
        }
    }

    private suspend fun Coin.toItem(itemType: CoinItem.ItemType, favorite: Boolean): CoinItem {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.currency
            val sort = pref.sort
            val order = pref.order
            when (itemType) {
                CoinItem.ItemType.ITEM -> {
                    CoinItem.getItem(input, formatter, currency, sort, order, favorite = favorite)
                }
                CoinItem.ItemType.INFO -> {
                    CoinItem.getInfoItem(
                        input,
                        formatter,
                        currency,
                        sort,
                        order,
                        favorite = favorite
                    )
                }
                CoinItem.ItemType.QUOTE -> {
                    CoinItem.getQuoteItem(
                        input,
                        formatter,
                        currency,
                        sort,
                        order,
                        favorite = favorite
                    )
                }
            }
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