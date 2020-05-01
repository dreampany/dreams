package com.dreampany.tools.ui.crypto.vm

import android.app.Application
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.data.source.crypto.repo.CoinRepo
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.misc.constant.CryptoConstants
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
    private val pref: CryptoPref,
    private val repo: CoinRepo
) : BaseViewModel<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Coin, CoinItem, UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Coin>>(
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
                result = repo.getCoins(currency, sort, order, offset, CryptoConstants.Limits.COINS)
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
            try {
                val currency = pref.getCurrency()
                result = repo.getCoin(id, currency)
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

    fun loadFavoriteCoins() {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Coin>? = null
            var errors: SmartError? = null
            try {
                val currency = pref.getCurrency()
                val sort = pref.getSort()
                val order = pref.getOrder()
                result = repo.getFavoriteCoins(currency, sort, order)
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

    fun toggleFavorite(input: Coin) {
        uiScope.launch {
            postProgressSingle(true)
            var result: Coin? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                favorite = repo.isFavorite(input)
                result = input
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite), state = CryptoState.FAVORITE)
            }
        }
    }

    private suspend fun List<Coin>.toItems(): List<CoinItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.getCurrency()
            val sort = pref.getSort()
            val order = pref.getOrder()
            input.map { CoinItem.getItem(it, formatter, currency, sort, order) }
        }
    }

    private suspend fun Coin.toItems(): List<CoinItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.getCurrency()
            val sort = pref.getSort()
            val order = pref.getOrder()
            val result = arrayListOf<CoinItem>()
            result.add(CoinItem.getInfoItem(input, formatter, currency, sort, order))
            result.add(CoinItem.getQuoteItem(input, formatter, currency, sort, order))
            result
        }
    }

    private suspend fun Coin.toItem(favorite: Boolean): CoinItem {
        val input = this
        return withContext(Dispatchers.IO) {
            val currency = pref.getCurrency()
            val sort = pref.getSort()
            val order = pref.getOrder()
            CoinItem.getItem(input, formatter, currency, sort, order, favorite = favorite)
        }
    }

    private fun postProgressSingle(progress: Boolean) {
        postProgressSingle(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            progress = progress
        )
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<CoinItem>?) {
        postMultiple(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun postResult(result: CoinItem?, state: CryptoState = CryptoState.DEFAULT) {
        postSingle(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            state,
            CryptoAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}