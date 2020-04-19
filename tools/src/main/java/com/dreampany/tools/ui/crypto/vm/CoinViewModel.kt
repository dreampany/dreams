package com.dreampany.tools.ui.crypto.vm

import android.app.Application
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.data.source.crypto.repo.CoinRepo
import com.dreampany.tools.misc.CurrencyFormatter
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
) : BaseViewModel<CryptoType, CryptoSubtype, State, Action, Coin, CoinItem, UiTask<CryptoType, CryptoSubtype, State, Action, Coin>>(
    application,
    rm
) {

    fun loadCoins(offset: Long) {
        uiScope.launch {
            postProgress(true)
            var result: List<Coin>? = null
            var errors: SmartError? = null
            try {
                val currency = pref.getCurrency()
                val sort = pref.getSort()
                val order = pref.getOrder()
                result = repo.getItems(currency, sort, order, offset, CryptoConstants.Limits.COINS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else if (result != null) {
                postResult(result.toItems())
            }
        }
    }

    fun toggleFavorite(item: CoinItem) {

    }

   private suspend fun List<Coin>.toItems(): List<CoinItem> {
        val list = this
        return withContext(Dispatchers.IO) {
            val currency = pref.getCurrency()
            val sort = pref.getSort()
            val order = pref.getOrder()
            list.map { CoinItem(it, formatter, currency, sort, order) }
        }
    }

    private fun postProgress(progress: Boolean) {
        postProgressMultiple(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<CoinItem>) {
        postMultiple(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}