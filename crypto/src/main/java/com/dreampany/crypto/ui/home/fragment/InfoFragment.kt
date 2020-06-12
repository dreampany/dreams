package com.dreampany.crypto.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.*
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.data.source.pref.AppPref
import com.dreampany.crypto.databinding.CoinInfoFragmentBinding
import com.dreampany.crypto.misc.func.CurrencyFormatter
import com.dreampany.crypto.ui.home.model.CoinItem
import com.dreampany.crypto.ui.home.vm.CoinViewModel
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.context
import com.dreampany.framework.misc.exts.init
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class InfoFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var pref: AppPref

    @Inject
    internal lateinit var formatter: CurrencyFormatter

    private lateinit var bind: CoinInfoFragmentBinding
    private lateinit var vm: CoinViewModel
    private lateinit var input: Coin

    override val layoutRes: Int = R.layout.coin_info_fragment

    override fun onStartUi(state: Bundle?) {
        val task: UiTask<Type, Subtype, State, Action, Coin> =
            (task ?: return) as UiTask<Type, Subtype, State, Action, Coin>
        input = task.input ?: return
        initUi()
        onRefresh()
    }

    override fun onStopUi() {
    }

    override fun onRefresh() {
        loadCoin()
    }

    private fun onItemPressed(view: View, item: CoinItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.button_favorite -> {
                onFavoriteClicked(item)
            }
        }
    }

    private fun onFavoriteClicked(item: CoinItem) {
        vm.toggleFavorite(item.input, CoinItem.ItemType.INFO)
    }

    private fun loadCoin() {
        if (::input.isInitialized)
            vm.loadCoin(input.id)
    }

    private fun initUi() {
        bind = getBinding()
        bind.swipe.init(this)
        vm = createVm(CoinViewModel::class)
        vm.subscribe(this, Observer { this.processResponse(it) })
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, CoinItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, CoinItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResult(result: CoinItem?) {
        if (result != null) {
            input = result.input
            result.quote?.let {
                bind.layoutInfo.layoutPrice.textPrice.text = formatter.format(it.currency, it.price)
                bind.layoutInfo.layoutPrice.textCap.text =
                    formatter.roundPrice(R.string.format_symbol_price, it.getMarketCap(), it.currency)
                bind.layoutInfo.layoutPrice.textChange.text = bind.context.getString(
                    R.string.format_24h_change_price,
                    formatter.format(it.currency, it.getChange24h())
                )
                bind.layoutInfo.layoutPrice.textChange.setTextColor(formatter.getColor(it.getChange24h()))
            }
            bind.layoutInfo.layoutPrice.textSupply.text =
                formatter.roundPrice(R.string.format_supply_price, input.getCirculatingSupply(), result.currency)
        }
    }
}