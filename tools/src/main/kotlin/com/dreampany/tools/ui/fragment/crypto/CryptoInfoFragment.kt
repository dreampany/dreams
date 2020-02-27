package com.dreampany.tools.ui.fragment.crypto

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.injector.annote.FragmentScope
import com.dreampany.framework.misc.extension.inflate
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentRecyclerBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.crypto.CoinAdapter
import com.dreampany.tools.ui.model.CoinItem
import com.dreampany.tools.ui.vm.CoinViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager
import javax.inject.Inject

/**
 * Created by roman on 2/26/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class CryptoInfoFragment
@Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var bind: FragmentRecyclerBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var adapter: CoinAdapter

    private lateinit var vm: CoinViewModel

    override fun getScreen(): String {
        return Constants.cryptoInfo(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recycler
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
    }

    override fun onStopUi() {

    }

    private fun initUi() {
        bind = super.binding as FragmentRecyclerBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        ViewUtil.setSwipe(bind.layoutRefresh, this)

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        bind.stateful.setStateView(
            UiState.EMPTY.name,
            context.inflate(R.layout.content_empty).apply {
                // setOnClickListener(this@Crypto)
            }
        )

        vm = ViewModelProvider(this, factory).get(CoinViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }


    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = CoinAdapter(listener = this)
        adapter.setStickyHeaders(false)
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollStaggeredLayoutManager(context!!, adapter.getSpanCount()),
            FlexibleItemDecoration(context!!)
                .withOffset(adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
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
            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<CoinItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSingleResponse(response: Response<CoinItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<CoinItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, item: CoinItem) {
        adapter.addItem(item)
    }

    private fun processSuccess(state: State, action: Action, items: List<CoinItem>) {
        adapter.addItems(items)
        ex.postToUi(Runnable {
            vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
    }
}