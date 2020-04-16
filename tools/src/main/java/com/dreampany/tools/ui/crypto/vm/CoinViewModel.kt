package com.dreampany.tools.ui.crypto.vm

import android.app.Application
import com.dreampany.common.data.enums.Order
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.home.Subtype
import com.dreampany.tools.data.enums.home.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.data.source.crypto.repo.CoinRepo
import com.dreampany.tools.misc.CurrencyFormatter
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
    private val pref: CryptoPref,
    private val repo: CoinRepo
) : BaseViewModel<Type, Subtype, State, Action, Coin, CoinItem, UiTask<Type, Subtype, State, Action, Coin>>(
    application,
    rm
) {

    private lateinit var formatter: CurrencyFormatter
    private lateinit var currency: Currency
    private lateinit var sort: CoinSort
    private lateinit var order: Order

    fun setProperty(
        formatter: CurrencyFormatter,
        currency: Currency,
        sort: CoinSort,
        order: Order,
        reset: Boolean = false
    ) {
        this.formatter = formatter
        this.currency = currency
        this.sort = sort
        this.order = order
        if (reset) {
            //clearAll()
        }
    }

    fun loadCoins(offset: Long, limit: Long) {
        uiScope.launch {
            postProgressMultiple(
                Type.COIN,
                Subtype.DEFAULT,
                State.DEFAULT,
                Action.DEFAULT,
                progress = true
            )
            var result: List<Coin>? = null
            var errors: Throwable? = null
            try {
                val currency = pref.getCurrency()
                val sort = pref.getSort()
                val order = pref.getOrder()
                result = repo.getItems(currency, sort, order, offset, limit)
            } catch (error: Throwable) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postMultiple(
                    Type.COIN,
                    Subtype.DEFAULT,
                    State.DEFAULT,
                    Action.DEFAULT,
                    error = errors,
                    showProgress = true
                )
            } else if (result != null) {
                postMultiple(
                    Type.COIN,
                    Subtype.DEFAULT,
                    State.DEFAULT,
                    Action.DEFAULT,
                    result = result.toItems(),
                    showProgress = true
                )
            }
        }
    }

    suspend fun List<Coin>.toItems(): List<CoinItem> {
        val list = this
        return withContext(Dispatchers.IO) {
            list.map { CoinItem(it, formatter, currency, sort, order) }
        }
    }
}