package com.dreampany.tools.ui.fragment.vpn

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Country
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.CountryMapper
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentCountriesBinding
import com.dreampany.tools.databinding.FragmentServersBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.CountryAdapter
import com.dreampany.tools.ui.adapter.ServerAdapter
import com.dreampany.tools.ui.misc.CountryRequest
import com.dreampany.tools.ui.misc.ServerRequest
import com.dreampany.tools.ui.model.CountryItem
import com.dreampany.tools.ui.model.ServerItem
import com.dreampany.tools.ui.vm.vpn.CountryViewModel
import com.dreampany.tools.ui.vm.vpn.ServerViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2020-01-04
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class CountriesFragment
@Inject constructor() : BaseFragment(),
    SmartAdapter.OnUiItemClickListener<CountryItem, Action> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var mapper: CountryMapper
    @Inject
    internal lateinit var session: SessionManager

    private lateinit var bind: FragmentCountriesBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var vm: CountryViewModel
    private lateinit var adapter: CountryAdapter
    private lateinit var scroller: OnVerticalScrollListener

    override fun getLayoutId(): Int {
        return R.layout.fragment_countries
    }

    override fun getTitleResId(): Int {
        return R.string.vpn_countries
    }

    override fun getScreen(): String {
        return Constants.vpnCountries(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        if (adapter.isEmpty) {
            request(progress = true)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onRefresh() {
        super.onRefresh()
        if (adapter.isEmpty) {
            request(progress = true)
        } else {
            vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!isOkay(resultCode)) {
            return
        }
        when (requestCode) {
            Constants.RequestCode.Vpn.OPEN_SERVER -> {
                data?.run {
                    val task = getCurrentTask<UiTask<Server>>(this)
                    task?.run {
                        task.input?.run {
                            Timber.v("Selected Server %s", this.id)
                            val uiTask = UiTask<Server>(
                                type = Type.COUNTRY,
                                action = Action.SELECTED,
                                input = this
                            )
                            forResult(uiTask, true)
                        }
                    }
                }
            }
        }
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (bind.layoutRefresh.isRefreshing) return false
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText)
            adapter.filterItems()
        }
        return false
    }

    override fun onUiItemClick(view: View, item: CountryItem, action: Action) {
        openServersUi(item)
    }

    override fun onUiItemLongClick(view: View, item: CountryItem, action: Action) {

    }


    private fun initUi() {
        bind = super.binding as FragmentCountriesBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bind.fab.setOnClickListener(this)

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty, null).apply {
                setOnClickListener(this@CountriesFragment)
            }
        )

        vm = ViewModelProvider(this, factory).get(CountryViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        //vm.observeOutput(this, Observer { this.processSingleResponse(it) })
        vm.updateUiState(uiState = UiState.DEFAULT)

        // countryCode = GeoUtil.getCountryCode(context!!)
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = CountryAdapter(listener = this)
        adapter.setStickyHeaders(false)
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_country, adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun processUiState(response: Response.UiResponse) {
        Timber.v("UiState %s", response.uiState.name)
        when (response.uiState) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
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
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
                //initTitleSubtitle()
            }
        }
    }

    fun processMultipleResponse(response: Response<List<CountryItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<CountryItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<CountryItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        ex.postToUi(Runnable {
            vm.updateUiState(state, action, UiState.EXTRA)
        }, 500L)
    }

    private fun request(
        id: String? = Constants.Default.NULL,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        input: Country? = Constants.Default.NULL
    ) {

        val request = CountryRequest(
            id = id,
            type = Type.COUNTRY,
            subtype = Subtype.DEFAULT,
            action = action,
            single = single,
            progress = progress,
            input = input,
            limit = Constants.Limit.Vpn.COUNTRIES
        )
        vm.request(request)
    }

    private fun openServersUi(item: CountryItem) {
        val task = UiTask<Server>(
            id = item.item.id,
            type = Type.SERVER,
            state = State.LIST,
            action = Action.OPEN
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.Vpn.OPEN_SERVER)
    }
}