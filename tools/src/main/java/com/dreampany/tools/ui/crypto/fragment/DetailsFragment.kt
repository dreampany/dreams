package com.dreampany.tools.ui.crypto.fragment

import android.os.Bundle
import android.view.View
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.init
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Quote
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.databinding.CryptoDetailsFragmentBinding
import com.dreampany.tools.databinding.RecyclerFragmentBinding
import com.dreampany.tools.misc.func.CurrencyFormatter
import com.dreampany.tools.ui.crypto.adapter.FastCoinAdapter
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class DetailsFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var pref: Prefs

    @Inject
    internal lateinit var formatter: CurrencyFormatter

    private lateinit var bind: CryptoDetailsFragmentBinding
    private lateinit var vm: CoinViewModel
    private lateinit var input: Coin
    private lateinit var quote: Quote

    override val layoutRes: Int = R.layout.crypto_details_fragment

    override fun onStartUi(state: Bundle?) {
        val task  = (task ?: return) as UiTask<Type, Subtype, State, Action, Coin>
        input = task.input ?: return
        initUi()
        //onRefresh()
    }

    override fun onStopUi() {
        //adapter.destroy()
    }


    override fun onRefresh() {
        //loadCoin()
    }

    fun updateUi(input : Coin, quote: Quote) {
        this.input = input
        this.quote = quote
        loadUi()
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
        vm.toggleFavorite(item.input)
    }

    private fun loadCoin() {
        if (::input.isInitialized)
            vm.loadCoin(input.id)
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(CoinViewModel::class)

        vm.subscribe(this,   { this.processResponse(it) })
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, CoinItem>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            process(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, CoinItem>) {
            Timber.v("Result [%s]", response.result)
            process(response.result)
        }
    }

    private fun process(error: SmartError) {
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

    private fun process(result: CoinItem?) {
        if (result != null) {

        }
    }

    private fun loadUi() {
        val currency = pref.currency
        
        bind.volume24hLayout.title.setText(R.string.volume_24h)
        bind.volume24hLayout.price.text = formatter.roundPrice(quote.getVolume24h(), currency)
        
        bind.volume7dLayout.title.setText(R.string.volume_7d)
        bind.volume7dLayout.price.text = formatter.roundPrice(quote.getVolume7d(), currency)

        bind.volume30dLayout.title.setText(R.string.volume_30d)
        bind.volume30dLayout.price.text = formatter.roundPrice(quote.getVolume30d(), currency)

        bind.circulatingSupplyLayout.title.setText(R.string.circulating_supply)
        bind.circulatingSupplyLayout.price.text = formatter.roundPrice(input.getCirculatingSupply(), currency)

        bind.totalSupplyLayout.title.setText(R.string.total_supply)
        bind.totalSupplyLayout.price.text = formatter.roundPrice(input.getTotalSupply(), currency)

        bind.maxSupplyLayout.title.setText(R.string.max_supply)
        bind.maxSupplyLayout.price.text = formatter.roundPrice(input.getMaxSupply(), currency)
    }

}