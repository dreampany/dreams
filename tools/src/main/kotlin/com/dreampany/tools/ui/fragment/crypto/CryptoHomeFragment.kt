package com.dreampany.tools.ui.fragment.crypto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.callback.SearchViewCallback
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.MenuTint
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.data.source.pref.CryptoPref
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentCryptoHomeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.CoinAdapter
import com.dreampany.tools.ui.misc.CoinRequest
import com.dreampany.tools.ui.model.CoinItem
import com.dreampany.tools.ui.vm.CoinViewModel
import com.ferfalk.simplesearchview.SimpleSearchView
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class CryptoHomeFragment
@Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var cryptoPref: CryptoPref
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var bind: FragmentCryptoHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var searchView: SimpleSearchView

    private lateinit var vm: CoinViewModel
    private lateinit var adapter: CoinAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_crypto_home
    }

    override fun getMenuId(): Int {
        return R.menu.menu_crypto_home
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_crypto
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val searchItem = getSearchMenuItem()
        val favoriteItem = menu.findItem(R.id.item_favorite)
        val settingsItem = menu.findItem(R.id.item_settings)
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context!!, R.color.material_white),
            null, searchItem, favoriteItem, settingsItem
        )

        val activity = getParent()

        if (activity is SearchViewCallback) {
            val searchCallback = activity as SearchViewCallback?
            searchView = searchCallback!!.searchView
            val searchItem = getSearchMenuItem()
            searchItem?.run {
                //initSearchView(searchView, this)
            }
        }
        //initLanguageUi()
    }

    override fun getScreen(): String {
        return Constants.cryptoHome(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        request(
            state = State.PAGINATED,
            action = Action.GET,
            single = false,
            progress = true,
            start = adapter.itemCount.toLong(),
            limit = Constants.Limit.Crypto.LIST
        )
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        if (searchView.isSearchOpen()) {
            searchView.closeSearch()
        }
    }

    override fun onRefresh() {
/*        request(
            state = State.LIST,
            action = Action.GET,
            single = false,
            progress = true,
            start = adapter.itemCount.toLong(),
            limit = Constants.Limit.Crypto.LIST
        )*/
/*        if (adapter.isEmpty) {
            request(
                state = State.LIST,
                action = Action.GET,
                single = false,
                progress = true,
                limit = Constants.Limit.Crypto.LIST
            )
        } else {
            vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        }*/
    }

    private fun initUi() {
        bind = super.binding as FragmentCryptoHomeBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )
        bind.stateful.setStateView(
            UiState.SEARCH.name,
            LayoutInflater.from(context).inflate(R.layout.item_search, null)
        )
        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty, null)
        )

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bind.fab.setOnClickListener(this)

        vm = ViewModelProvider(this, factory).get(CoinViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = CoinAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {
            override fun onScrollingAtEnd() {

            }

            override fun onScrolledToBottom() {
                request(
                    state = State.PAGINATED,
                    action = Action.GET,
                    single = false,
                    progress = true,
                    start = adapter.itemCount.toLong(),
                    limit = Constants.Limit.Crypto.LIST
                )
            }
        }
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_coin, vm.itemOffset)
                .withEdge(true),
            null,
            scroller, null
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
            UiState.EMPTY -> bind.stateful.setState(UiState.SEARCH.name)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun processResponse(response: Response<List<CoinItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<CoinItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSingleResponse(response: Response<CoinItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<CoinItem>
            processSingleSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<CoinItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        val currency = cryptoPref.getCurrency()
        val sort = cryptoPref.getSort()
        val order = cryptoPref.getOrder()
        adapter.addItems(currency, sort, order, items)
        ex.postToUi(Runnable {
            vm.updateUiState(state, action, UiState.EXTRA)
        }, 500L)
    }

    private fun processSingleSuccess(state: State, action: Action, item: CoinItem) {
        Timber.v("Result Single Coin[%s]", item.item.id)

    }

    private fun request(
        id: String = Constants.Default.STRING,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        start: Long = Constants.Default.LONG,
        limit: Long = Constants.Default.LONG
    ) {
        val currency = cryptoPref.getCurrency()
        val sort = cryptoPref.getSort()
        val order = cryptoPref.getOrder()
        val request = CoinRequest(
            id = id,
            currency = currency,
            sort = sort,
            order = order,
            type = Type.COIN,
            subtype = Subtype.DEFAULT,
            state = state,
            action = action,
            single = single,
            progress = progress,
            start = start,
            limit = limit
        )
        vm.request(request)
    }
}