package com.dreampany.tools.ui.crypto.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Trade
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.databinding.CoinMarketFragmentBinding
import com.dreampany.tools.ui.crypto.adapter.FastExchangeAdapter
import com.dreampany.tools.ui.crypto.model.ExchangeItem
import com.dreampany.tools.ui.crypto.vm.ExchangeViewModel
import com.dreampany.tools.ui.crypto.vm.TradeViewModel
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class MarketFragment
@Inject constructor() : InjectFragment(), OnMenuItemClickListener<PowerMenuItem> {

    @Inject
    internal lateinit var cryptoPref: CryptoPref

    private lateinit var bind: CoinMarketFragmentBinding
    private lateinit var adapter: FastExchangeAdapter
    private lateinit var tvm: TradeViewModel
    private lateinit var evm: ExchangeViewModel

    private lateinit var input: Coin

    private lateinit var toSymbol: String
    private var trades: List<Trade>? = null

    private val toSymbolItems = arrayListOf<PowerMenuItem>()
    private var toSymbolMenu: PowerMenu? = null


    override val layoutRes: Int = R.layout.coin_market_fragment

    override fun onStartUi(state: Bundle?) {
        val task: UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Coin> =
            (task ?: return) as UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Coin>
        input = task.input ?: return
        initUi()
        initRecycler(state)
        toSymbol = getString(R.string.usd)
        onRefresh()
    }

    override fun onStopUi() {
        adapter.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRefresh() {
        if (trades?.isEmpty() ?: true) {
            loadTrades()
            return
        } else if (adapter.isEmpty) {
            loadExchanges()
            return
        }
        bind.swipe.refresh(false)
    }

    override fun onItemClick(position: Int, item: PowerMenuItem) {
        toSymbolMenu?.dismiss()
        val trade: Trade = item.tag as Trade
        Timber.v("Trade ToSymbol fired %s", trade.toString())
        processTradeSelection(trade)
    }

    private fun loadTrades() {
        if (::input.isInitialized) {
            val fromSymbol = input.symbol ?: return
            val extraParams = getString(R.string.app_name)
            tvm.loadTrades(fromSymbol, extraParams)
        }
    }

    private fun loadExchanges() {
        if (::input.isInitialized) {
            val fromSymbol = input.symbol ?: return
            val extraParams = getString(R.string.app_name)
            evm.loadExchanges(fromSymbol, toSymbol, extraParams)
        }
    }

    private fun initUi() {
        bind = getBinding()
        tvm = createVm(TradeViewModel::class)
        evm = createVm(ExchangeViewModel::class)
        tvm.subscribes(this, Observer { this.processResponsesOfTrade(it) })
        evm.subscribes(this, Observer { this.processResponsesOfExchange(it) })

        bind.buttonFromSymbol.text = input.symbol
        bind.swipe.init(this)
        bind.buttonToSymbol.setOnSafeClickListener {
            openOptionsMenu(it)
        }
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastExchangeAdapter()
        }

        adapter.initRecycler(
            state,
            bind.recycler
        )
    }

    private fun processResponsesOfTrade(response: Response<CryptoType, CryptoSubtype, CryptoState, CryptoAction, List<Trade>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<CryptoType, CryptoSubtype, CryptoState, CryptoAction, List<Trade>>) {
            Timber.v("Result [%s]", response.result)
            processResultsOfTrade(response.result)
        }
    }

    private fun processResponsesOfExchange(response: Response<CryptoType, CryptoSubtype, CryptoState, CryptoAction, List<ExchangeItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<CryptoType, CryptoSubtype, CryptoState, CryptoAction, List<ExchangeItem>>) {
            Timber.v("Result [%s]", response.result)
            processResultsOfExchange(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message = if (error.hostError) getString(R.string.message_no_internet) else error.message
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

    private fun processResultsOfTrade(result: List<Trade>?) {
        if (result != null) {
            trades = result
            buildToSymbolItems()
            loadExchanges()
        }
    }

    private fun processResultsOfExchange(result: List<ExchangeItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }
    }

    private fun buildToSymbolItems(fresh: Boolean = false) {
        if (fresh) {
            toSymbolItems.clear()
        }
        if (toSymbolItems.isNotEmpty()) {
            return
        }
        trades?.forEach { item ->
            toSymbolItems.add(
                PowerMenuItem(
                    item.getToSymbol(),
                    toSymbol.equals(item.getToSymbol()),
                    item
                )
            )
        }
    }

    private fun openOptionsMenu(view: View) {
        context?.let {
            toSymbolMenu = PowerMenu.Builder(it)
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                .addItemList(toSymbolItems)
                .setSelectedMenuColor(it.color(R.color.colorPrimary))
                .setSelectedTextColor(Color.WHITE)
                .setOnMenuItemClickListener(this)
                .setLifecycleOwner(this)
                .setDividerHeight(1)
                .setTextSize(14)
                .build()
        }
        toSymbolMenu?.showAsAnchorLeftBottom(view)
    }

    private fun processTradeSelection(trade: Trade) {
        if (!toSymbol.equals(trade.getToSymbol())) {
            toSymbol = trade.getToSymbol()
            bind.buttonToSymbol.text = toSymbol
            buildToSymbolItems(true)
            loadExchanges()
        }
    }
}