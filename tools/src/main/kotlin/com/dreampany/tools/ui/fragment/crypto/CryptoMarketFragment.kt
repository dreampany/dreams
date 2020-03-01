package com.dreampany.tools.ui.fragment.crypto

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.injector.annote.FragmentScope
import com.dreampany.framework.misc.extension.inflate
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.ViewUtil
import com.dreampany.language.Language
import com.dreampany.tools.R
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.crypto.Trade
import com.dreampany.tools.data.source.pref.CryptoPref
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentCryptoMarketBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.crypto.ExchangeAdapter
import com.dreampany.tools.ui.model.crypto.CoinItem
import com.dreampany.tools.ui.model.crypto.ExchangeItem
import com.dreampany.tools.ui.model.crypto.TradeItem
import com.dreampany.tools.ui.request.crypto.ExchangeRequest
import com.dreampany.tools.ui.request.crypto.TradeRequest
import com.dreampany.tools.ui.vm.crypto.ExchangeViewModel
import com.dreampany.tools.ui.vm.crypto.TradeViewModel
import com.google.android.material.card.MaterialCardView
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class CryptoMarketFragment
@Inject constructor() : BaseFragment(), OnMenuItemClickListener<PowerMenuItem> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var cryptoPref: CryptoPref

    private lateinit var bind: FragmentCryptoMarketBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var adapter: ExchangeAdapter

    private lateinit var tvm: TradeViewModel
    private lateinit var evm: ExchangeViewModel
    private lateinit var coin: Coin

    private lateinit var toSymbol: String
    private lateinit var tradeItems: List<TradeItem>

    private val toSymbolItems = ArrayList<PowerMenuItem>()
    private var toSymbolMenu: PowerMenu? = null

    override fun getScreen(): String {
        return Constants.cryptoMarket(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_crypto_market
    }

    override fun onStartUi(state: Bundle?) {
        val task = getCurrentTask<UiTask<Coin>>() ?: return
        coin = task.input ?: return
        toSymbol = getString(R.string.usd)
        initUi()
        initRecycler()
        requestTrades(progress = true, limit = Constants.Limit.Crypto.TRADES)
    }

    override fun onStopUi() {
        tvm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        evm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    override fun onRefresh() {
        if (adapter.isEmpty) {
            if (::coin.isInitialized)
                requestExchanges(progress = true, limit = Constants.Limit.Crypto.EXCHANGES)
        } else {
            tvm.updateUiState(uiState = UiState.HIDE_PROGRESS)
            evm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        }
    }

    override fun onItemClick(position: Int, item: PowerMenuItem) {
        toSymbolMenu?.dismiss()
        val trade: Trade = item.tag as Trade
        Timber.v("Trade ToSymbol fired %s", trade.toString())
        processTradeSelection(trade)
    }

    private fun initUi() {
        bind = super.binding as FragmentCryptoMarketBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        ViewUtil.setSwipe(bind.layoutRefresh, this)

        bind.buttonToSymbol.setOnSafeClickListener {
            openOptionsMenu(it)
        }

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        bind.stateful.setStateView(
            UiState.EMPTY.name,
            context.inflate(R.layout.content_empty_crypto)
        )

        tvm = ViewModelProvider(this, factory).get(TradeViewModel::class.java)
        evm = ViewModelProvider(this, factory).get(ExchangeViewModel::class.java)
        tvm.observeUiState(this, Observer { this.processUiState(it) })
        evm.observeUiState(this, Observer { this.processUiState(it) })
        tvm.observeOutputs(this, Observer { this.processTradesResponse(it) })
        evm.observeOutputs(this, Observer { this.processExchangesResponse(it) })

        bind.buttonFromSymbol.text = coin.symbol
    }


    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = ExchangeAdapter(listener = this)
        adapter.setStickyHeaders(false)
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .withOffset(adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun requestTrades(
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        start: Long = Constants.Default.LONG,
        limit: Long = Constants.Default.LONG,
        id: String = Constants.Default.STRING,
        ids: List<String>? = Constants.Default.NULL
    ) {
        val extraParams = getString(R.string.app_name)
        val fromSymbol = coin.symbol ?: return
        val request = TradeRequest(
            type = Type.TRADE,
            subtype = Subtype.DEFAULT,
            action = action,
            single = single,
            progress = progress,
            start = start,
            limit = limit,
            id = id,
            ids = ids,
            extraParams = extraParams,
            fromSymbol = fromSymbol
        )
        tvm.request(request)
    }

    private fun requestExchanges(
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        start: Long = Constants.Default.LONG,
        limit: Long = Constants.Default.LONG,
        id: String = Constants.Default.STRING,
        ids: List<String>? = Constants.Default.NULL
    ) {
        val extraParams = getString(R.string.app_name)
        val fromSymbol = coin.symbol ?: return
        val request = ExchangeRequest(
            type = Type.TRADE,
            subtype = Subtype.DEFAULT,
            action = action,
            single = single,
            progress = progress,
            start = start,
            limit = limit,
            id = id,
            ids = ids,
            extraParams = extraParams,
            fromSymbol = fromSymbol,
            toSymbol = toSymbol
        )
        evm.request(request)
    }

    private fun processUiState(response: Response.UiResponse) {
        when (response.uiState) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> {
                response.uiState = if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT
                processUiState(response)
            }
            UiState.SEARCH -> bind.stateful.setState(UiState.SEARCH.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun processTradesResponse(response: Response<List<TradeItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            tvm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            tvm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<TradeItem>>
            processTradesSuccess(result.state, result.action, result.data)
        }
    }

    private fun processExchangesResponse(response: Response<List<ExchangeItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            tvm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            tvm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<ExchangeItem>>
            processExchangesSuccess(result.state, result.action, result.data)
        }
    }

    private fun processTradesSuccess(state: State, action: Action, items: List<TradeItem>) {
        tradeItems = items
        buildToSymbolItems(tradeItems)
        requestExchanges(progress = true, limit = Constants.Limit.Crypto.EXCHANGES)
    }

    private fun processExchangesSuccess(state: State, action: Action, items: List<ExchangeItem>) {
        adapter.clear()
        adapter.addItems(items)
        ex.postToUi(Runnable {
            evm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
    }

    private fun buildToSymbolItems(trades: List<TradeItem>, fresh: Boolean = false) {
        if (fresh) {
            toSymbolItems.clear()
        }
        if (toSymbolItems.isNotEmpty()) {
            return
        }
        trades.forEach { item ->
            toSymbolItems.add(
                PowerMenuItem(
                    item.item.getToSymbol(),
                    toSymbol.equals(item.item.getToSymbol()),
                    item.item
                )
            )
        }
    }

    private fun openOptionsMenu(view: View) {
        toSymbolMenu = PowerMenu.Builder(context)
            .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
            .addItemList(toSymbolItems)
            .setSelectedMenuColor(ColorUtil.getColor(context!!, R.color.colorPrimary))
            .setSelectedTextColor(Color.WHITE)
            .setOnMenuItemClickListener(this)
            .setLifecycleOwner(this)
            .setDividerHeight(1)
            .setTextSize(14)
            .build()
        toSymbolMenu?.showAsAnchorLeftBottom(view)
    }

    private fun processTradeSelection(trade: Trade) {
        if (!toSymbol.equals(trade.getToSymbol())) {
            toSymbol = trade.getToSymbol() ?: return
            bind.buttonToSymbol.text = toSymbol
            buildToSymbolItems(tradeItems, true)
            //TODO read exchanges data
            requestExchanges(progress = true, limit = Constants.Limit.Crypto.EXCHANGES)
        }
    }
}