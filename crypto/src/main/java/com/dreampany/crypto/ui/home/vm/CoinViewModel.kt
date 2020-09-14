package com.dreampany.crypto.ui.home.vm

import android.app.Application
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.data.source.pref.AppPref
import com.dreampany.crypto.data.source.repo.CoinRepo
import com.dreampany.crypto.misc.constants.AppConstants
import com.dreampany.crypto.misc.func.CurrencyFormatter
import com.dreampany.crypto.ui.home.model.CoinItem
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
class CoinViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val formatter: CurrencyFormatter,
    private val pref: AppPref,
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
                val currency = pref.getCurrency()
                val sort = pref.getSort()
                val order = pref.getOrder()
                result = repo.gets(currency, sort, order, offset, AppConstants.Limits.COINS)
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
                val currency = pref.getCurrency()
                result = repo.get(id, currency)
                result?.let { favorite = repo.isFavorite(it) }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems(favorite))
                //postResult(result?.toItem(CoinItem.ItemType.ITEM, favorite))
            }
        }
    }

    fun loadFavoriteCoins() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Coin>? = null
            var errors: SmartError? = null
            try {
                val currency = pref.getCurrency()
                val sort = pref.getSort()
                val order = pref.getOrder()
                result = repo.getFavorites(currency, sort, order)
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
            val currency = pref.getCurrency()
            val sort = pref.getSort()
            val order = pref.getOrder()
            input.map { input ->
                val favorite = repo.isFavorite(input)
                CoinItem.getItem(input, formatter, currency, sort, order, favorite)
            }
        }
    }

    private suspend fun Coin.toItems(favorite: Boolean): List<CoinItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.getCurrency()
            val sort = pref.getSort()
            val order = pref.getOrder()
            val result = arrayListOf<CoinItem>()
            result.add(CoinItem.getInfoItem(input, formatter, currency, sort, order, favorite))
            result.add(CoinItem.getQuoteItem(input, formatter, currency, sort, order, favorite))
            result
        }
    }

    private suspend fun Coin.toItem(itemType: CoinItem.ItemType, favorite: Boolean): CoinItem {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.getCurrency()
            val sort = pref.getSort()
            val order = pref.getOrder()
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