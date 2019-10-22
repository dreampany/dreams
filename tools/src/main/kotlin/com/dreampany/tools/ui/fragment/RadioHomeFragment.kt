package com.dreampany.tools.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.extension.toTitle
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.GeoUtil
import com.dreampany.framework.util.MenuTint
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.StationMapper
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.pref.RadioPref
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentRadioHomeBinding
import com.dreampany.tools.manager.PlayerManager
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.StationAdapter
import com.dreampany.tools.ui.misc.StationRequest
import com.dreampany.tools.ui.model.StationItem
import com.dreampany.tools.ui.vm.StationViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-10-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class RadioHomeFragment
@Inject constructor() : BaseMenuFragment(),
    SmartAdapter.OnUiItemClickListener<StationItem?, Action?> {

    @Inject
    internal lateinit var mapper: StationMapper
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    @Inject
    internal lateinit var radioPref: RadioPref
    @Inject
    internal lateinit var player: PlayerManager

    private lateinit var bind: FragmentRadioHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var vm: StationViewModel
    private lateinit var adapter: StationAdapter
    private lateinit var scroller: OnVerticalScrollListener

    private lateinit var state: State
    private lateinit var countryCode: String

    override fun getLayoutId(): Int {
        return R.layout.fragment_radio_home
    }

    override fun getMenuId(): Int {
        return R.menu.menu_radio
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_radio
    }

    override fun getScreen(): String {
        return Constants.radioHome(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val searchItem = getSearchMenuItem()
        val categoryItem = menu.findItem(R.id.item_category)
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context!!, R.color.material_white),
            null, searchItem, categoryItem
        )
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        initTitleSubtitle()
        request(progress = true)
        player.bind()
    }

    override fun onStopUi() {
        player.debind()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Constants.Service.PLAYER_SERVICE_UPDATE)
        bindLocalCast(serviceUpdateReceiver, filter)
    }

    override fun onPause() {
        debindLocalCast(serviceUpdateReceiver)
        if (!player.isPlaying()) {
            player.destroy()
        }
        super.onPause()
    }

    override fun onRefresh() {
        super.onRefresh()
        if (adapter.isEmpty) {
            request(progress = true)
        } else {
            processUiState(UiState.HIDE_PROGRESS)
        }
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText)
            adapter.filterItems()
        }
        return false
    }

    override fun onClick(view: View, item: StationItem?, action: Action?) {
        item?.run {
            val station = item.item
            Timber.v("Station [%s]", station.url)
            player.play(station)
        }
    }

    override fun onLongClick(view: View, item: StationItem?, action: Action?) {
    }

    private fun initTitleSubtitle() {
        if (context == null) return
        setTitle(R.string.title_feature_radio)
        when(state) {
            State.LOCAL->{
                val subtitle = getString(R.string.subtitle_radio, state.name.toTitle(), countryCode, adapter.itemCount)
                setSubtitle(subtitle)
            }
        }

    }

    private fun initUi() {
        bind = super.binding as FragmentRadioHomeBinding
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
                setOnClickListener(this@RadioHomeFragment)
            }
        )

        processUiState(UiState.DEFAULT)

        vm = ViewModelProviders.of(this, factory).get(StationViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

        state = radioPref.getStationState(State.LOCAL)
        when (state) {
            State.LOCAL -> {
                countryCode = GeoUtil.getCountryCode(context!!)
            }
        }
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = StationAdapter(listener = this)
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

    private fun processUiState(state: UiState) {
        Timber.v("UiState %s", state.name)
        when (state) {
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
            UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
                initTitleSubtitle()
            }
        }
    }

    fun processMultipleResponse(response: Response<List<StationItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            Timber.v("processMultipleResponse %s", result.loading)
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<StationItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    fun processSingleResponse(response: Response<StationItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            Timber.v("processSingleResponse %s", result.loading)
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<StationItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<StationItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        updatePlaying()
        ex.postToUi(Runnable { processUiState(UiState.EXTRA) }, 500L)
    }

    private fun processSuccess(state: State, action: Action, item: StationItem) {
        if (action == Action.DELETE) {
            adapter.removeItem(item)
        } else {
            adapter.addItem(item)
        }
        ex.postToUi(Runnable { processUiState(UiState.EXTRA) }, 500L)

    }

    private fun updatePlaying() {
        if (player.isPlaying()) {
            player.getStation()?.run {
                mapper.getUiItem(this.id)?.run {
                    adapter.setSelection(this, true)
                }
            }
        } else {
            adapter.clearSelection()
        }
    }

    private fun request(
        id: String? = Constants.Default.NULL,
        action: Action = Action.DEFAULT,
        input: Station? = Constants.Default.NULL,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {

        val request = StationRequest(
            id = id,
            countryCode = countryCode,
            state = state,
            action = action,
            input = input,
            single = single,
            progress = progress
        )
        vm.request(request)
    }

    private val serviceUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updatePlaying()
        }

    }
}