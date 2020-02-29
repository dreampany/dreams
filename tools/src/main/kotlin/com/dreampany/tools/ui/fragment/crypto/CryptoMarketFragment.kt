package com.dreampany.tools.ui.fragment.crypto

import android.os.Bundle
import android.view.LayoutInflater
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
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.pref.CryptoPref
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentCryptoMarketBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.crypto.CoinAdapter
import com.dreampany.tools.ui.model.crypto.CoinItem
import com.dreampany.tools.ui.request.crypto.ExchangeRequest
import com.dreampany.tools.ui.request.crypto.TradeRequest
import com.dreampany.tools.ui.vm.crypto.ExchangeViewModel
import com.dreampany.tools.ui.vm.crypto.TradeViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import javax.inject.Inject

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class CryptoMarketFragment
@Inject constructor() : BaseFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var cryptoPref: CryptoPref

    private lateinit var bind: FragmentCryptoMarketBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var adapter: CoinAdapter

    private lateinit var tvm: TradeViewModel
    private lateinit var evm: ExchangeViewModel
    private lateinit var coin: Coin

    override fun getScreen(): String {
        return Constants.cryptoMarket(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_crypto_market
    }

    override fun onStartUi(state: Bundle?) {
        val task = getCurrentTask<UiTask<Coin>>() ?: return
        coin = task.input ?: return
        initUi()
        initRecycler()
        requestTrades(progress = true)

        //onRefresh()
        /*if (::coin.isInitialized)
            setTitle(coin.name)*/
    }

    override fun onStopUi() {
        tvm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        evm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    override fun onRefresh() {
        /*if (adapter.isEmpty) {
            if (::coin.isInitialized)
                //request(single = true, progress = true, id = coin.id)
        } else {
            tvm.updateUiState(uiState = UiState.HIDE_PROGRESS)
            evm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        }*/
    }

    private fun initUi() {
        bind = super.binding as FragmentCryptoMarketBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        ViewUtil.setSwipe(bind.layoutRefresh, this)

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
        //vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        //vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }


    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = CoinAdapter(listener = this)
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
        val request = TradeRequest(
            type = Type.TRADE,
            subtype = Subtype.DEFAULT,
            action = action,
            single = single,
            progress = progress,
            start = start,
            limit = limit,
            id = id,
            ids = ids
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
        val currency = cryptoPref.getCurrency()
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
            currency = currency
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

    private fun processMultipleResponse(response: Response<List<CoinItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
/*            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )*/
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            //vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<CoinItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSingleResponse(response: Response<CoinItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
           /* vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )*/
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            //vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<CoinItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, item: CoinItem) {
        //val result = vm.getInfos(item)
        //adapter.addItems(result)
    }

    private fun processSuccess(state: State, action: Action, items: List<CoinItem>) {
        adapter.addItems(items)
        ex.postToUi(Runnable {
            //vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
    }
}