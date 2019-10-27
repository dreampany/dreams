package com.dreampany.tools.ui.fragment.vpn

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.FragmentScope
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.MenuTint
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.ServerMapper
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.pref.VpnPref
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentServersBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.ServerAdapter
import com.dreampany.tools.ui.misc.StationRequest
import com.dreampany.tools.ui.model.ServerItem
import com.dreampany.tools.ui.vm.ServerViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-10-23
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class ServersFragment
@Inject constructor() : BaseMenuFragment(),
    SmartAdapter.OnUiItemClickListener<ServerItem, Action> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var mapper: ServerMapper
    @Inject
    internal lateinit var session: SessionManager
    @Inject
    internal lateinit var vpnPref: VpnPref

    private lateinit var bind: FragmentServersBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var vm: ServerViewModel
    private lateinit var adapter: ServerAdapter
    private lateinit var scroller: OnVerticalScrollListener

    private var state: State? = null
    private lateinit var countryCode: String


    override fun getLayoutId(): Int {
        return R.layout.fragment_servers
    }

    override fun getMenuId(): Int {
        return R.menu.menu_servers
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun getScreen(): String {
        //takeState()
        return Constants.vpnServers(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val searchItem = getSearchMenuItem()
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context!!, R.color.material_white),
            null, searchItem
        )
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Constants.Service.PLAYER_SERVICE_UPDATE)
        //bindLocalCast(serviceUpdateReceiver, filter)
        if (adapter.isEmpty) {
            request(progress = true)
        }
    }

    override fun onPause() {
/*        debindLocalCast(serviceUpdateReceiver)
        if (!player.isPlaying()) {
            player.destroy()
        }*/
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

    override fun onQueryTextChange(newText: String): Boolean {
        if (bind.layoutRefresh.isRefreshing) return false
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText)
            adapter.filterItems()
        }
        return false
    }

    override fun onUiItemClick(view: View, item: ServerItem, action: Action) {
        item?.run {
            val station = item.item
            //Timber.v("Station [%s]", station.url)
            //player.play(station)
        }
    }

    override fun onUiItemLongClick(view: View, item: ServerItem, action: Action) {

    }

    private fun takeState() {
        if (state == null) {
            val uiTask = getCurrentTask<UiTask<Station>>(true)
            state = uiTask?.state ?: State.LOCAL
        }
    }

    private fun initUi() {
        bind = super.binding as FragmentServersBinding
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
                setOnClickListener(this@ServersFragment)
            }
        )

        vm = ViewModelProvider(this, factory).get(ServerViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
        vm.updateUiState(uiState =  UiState.DEFAULT)

       // countryCode = GeoUtil.getCountryCode(context!!)
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = ServerAdapter(listener = this)
        adapter.setStickyHeaders(false)
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_station, adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun processUiState(response: Response.UiResponse) {
        Timber.v("UiState %s", response.uiState.name)
        if (this.state != response.state) {
            return
        }
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

    fun processMultipleResponse(response: Response<List<ServerItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            if (this.state != result.state) {
                return
            }
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            if (this.state != result.state) {
                return
            }
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<ServerItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    fun processSingleResponse(response: Response<ServerItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            if (this.state != result.state) {
                return
            }
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            if (this.state != result.state) {
                return
            }
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<ServerItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<ServerItem>) {
        if (this.state != state) {
            return
        }
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        ex.postToUi(Runnable {
            vm.updateUiState(state, action, UiState.EXTRA)
        }, 500L)
    }

    private fun processSuccess(state: State, action: Action, item: ServerItem) {
        if (this.state != state) {
            return
        }
        if (action == Action.DELETE) {
            adapter.removeItem(item)
        } else {
            adapter.addItem(item)
        }

        ex.postToUi(Runnable {
            vm.updateUiState(state, action, UiState.EXTRA)
        }, 500L)
    }


    private fun request(
        id: String? = Constants.Default.NULL,
        action: Action = Action.DEFAULT,
        input: Station? = Constants.Default.NULL,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {

        takeState()

        val request = StationRequest(
            id = id,
            countryCode = countryCode,
            state = state!!,
            action = action,
            single = single,
            progress = progress,
            input = input,
            limit = Constants.Limit.Radio.STATIONS
        )
        //vm.request(request)
    }

/*    private val serviceUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updatePlaying()
        }
    }*/
}