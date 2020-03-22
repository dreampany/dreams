package com.dreampany.tools.ui.crypto.vm

import android.app.Application
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.data.source.crypto.repo.CoinRepo
import kotlinx.coroutines.launch
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
) : BaseViewModel<Coin, Coin, UiTask<Coin, Type, Subtype>, Type, Subtype>(application, rm) {


    fun loadCoins(offset: Long, limit: Long) {
        uiScope.launch {
            postProgressMultiple(Type.COIN, Subtype.DEFAULT, progress = true)
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
                postMultiple(Type.COIN, Subtype.DEFAULT, error = errors, showProgress = true)
            } else if (result != null) {
                postMultiple(Type.COIN, Subtype.DEFAULT, result = result, showProgress = true)
            }
        }
    }
}