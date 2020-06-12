package com.dreampany.tools.ui.crypto.vm

import android.app.Application
import com.dreampany.framework.api.notify.NotifyManager
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.data.source.crypto.repo.CoinRepo
import com.dreampany.tools.misc.constant.CryptoConstants
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.ui.home.activity.HomeActivity
import kotlinx.coroutines.*
import org.apache.commons.lang3.RandomUtils
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 31/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CryptoViewModel
@Inject constructor(
    private val app: Application,
    private val rm: ResponseMapper,
    private val notify: NotifyManager,
    private val formatter: CurrencyFormatter,
    private val pref: CryptoPref,
    private val repo: CoinRepo
) {
    protected val job: Job
    protected val uiScope: CoroutineScope

    init {
        job = SupervisorJob()
        uiScope = CoroutineScope(Dispatchers.Main + job)
    }

    fun close() {
        job.cancel()
    }

    fun notifyProfitableCoin() {
        uiScope.launch {
            var result: List<Coin>? = null
            var errors: SmartError? = null
            val currency = pref.getCurrency()
            try {
                val sort = pref.getSort()
                val order = pref.getOrder()
                val offset = getRandOffset(CryptoConstants.Limits.COINS)
                result = repo.gets(currency, sort, order, offset, CryptoConstants.Limits.COINS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                //postError(errors)
            } else {
                //postResult(result?.toItems())
                val result = result?.maxBy { it.getQuote(currency)?.getChange24h().value() }
                if (result != null) {
                    showNotification(result)
                }
            }
        }
    }

    private fun getRandOffset(limit: Long): Long {
        val max = CryptoConstants.Limits.MAX_COINS - limit
        return RandomUtils.nextLong(0, max - 1)
    }

    private fun showNotification(coin: Coin) {
        val currency = pref.getCurrency()
        val quote = coin.getQuote(currency) ?: return

        val price = quote.price
        val dayChange: Double = quote.getChange24h()

        val title: String = app.getString(R.string.notify_title_profit)
        val message: String = formatter.formatPrice(
            coin.symbol.value(),
            coin.name.value(),
            price,
            dayChange,
            currency
        )
        val task = UiTask(
            CryptoType.COIN,
            CryptoSubtype.DEFAULT,
            CryptoState.DEFAULT,
            CryptoAction.VIEW,
            coin
        )
        notify.showNotification(
            title,
            message,
            R.drawable.ic_notification,
            HomeActivity::class.java,
            task
        )
    }
}