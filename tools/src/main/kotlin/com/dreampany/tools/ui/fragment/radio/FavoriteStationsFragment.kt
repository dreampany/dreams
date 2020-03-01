package com.dreampany.tools.ui.fragment.radio

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
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.misc.extensions.toTint
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.util.GeoUtil
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.StationMapper
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.pref.RadioPref
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentStationsBinding
import com.dreampany.tools.manager.PlayerManager
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.StationAdapter
import com.dreampany.tools.ui.request.StationRequest
import com.dreampany.tools.ui.model.StationItem
import com.dreampany.tools.ui.vm.radio.StationViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-10-23
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class FavoriteStationsFragment
@Inject constructor() : BaseMenuFragment(),
    SmartAdapter.OnUiItemClickListener<StationItem, Action> {

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

    private lateinit var bind: FragmentStationsBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var vm: StationViewModel
    private lateinit var adapter: StationAdapter
    private lateinit var scroller: OnVerticalScrollListener

    private lateinit var countryCode: String
    private var updated: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.fragment_stations
    }

    override fun getMenuId(): Int {
        return R.menu.menu_search
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun getTitleResId(): Int {
        return R.string.title_favorite_stations
    }

    override fun getScreen(): String {
        return Constants.favoriteStations(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val searchItem = getSearchMenuItem()
        searchItem.toTint(context, R.color.material_white)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        session.track()
        player.bind()
        initTitleSubtitle()
    }

    override fun onStopUi() {
        player.debind()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Constants.Service.PLAYER_SERVICE_UPDATE)
        bindLocalCast(serviceUpdateReceiver, filter)
        if (adapter.isEmpty) {
            request(action = Action.FAVORITE,  progress = true)
        }
    }

    override fun onPause() {
        debindLocalCast(serviceUpdateReceiver)
        if (!player.isPlaying()) {
            player.destroy()
        }
        super.onPause()
    }

    override fun hasBackPressed(): Boolean {
        forResult(updated)
        return true
    }

    override fun onRefresh() {
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

    override fun onUiItemClick(view: View, item: StationItem, action: Action) {
        when (action) {
            Action.OPEN -> {
                Timber.v("Station [%s]", item.item.url)
                player.play(item.item)
            }
            Action.FAVORITE -> {
                performFavorite(item.item)
            }
        }
    }

    override fun onUiItemLongClick(view: View, item: StationItem, action: Action) {

    }

    private fun initTitleSubtitle() {
        val subtitle = getString(R.string.subtitle_favorite_stations, adapter.itemCount)
        setSubtitle(subtitle)
    }

    private fun initUi() {
        bind = super.binding as FragmentStationsBinding
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
            LayoutInflater.from(context).inflate(R.layout.content_empty, null).apply {
                setOnClickListener(this@FavoriteStationsFragment)
            }
        )

        vm = ViewModelProvider(this, factory).get(StationViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
        vm.updateUiState( uiState = UiState.DEFAULT)

        countryCode = GeoUtil.getCountryCode(context!!)
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = StationAdapter(listener = this)
        adapter.setStickyHeaders(false)
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollStaggeredLayoutManager(context!!, adapter.getSpanCount()),
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
                initTitleSubtitle()
            }
        }
    }

    fun processMultipleResponse(response: Response<List<StationItem>>) {
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
            val result = response as Response.Result<List<StationItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    fun processSingleResponse(response: Response<StationItem>) {
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
            val result = response as Response.Result<StationItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<StationItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        updatePlaying()
        ex.postToUi(Runnable {
            vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
    }

    private fun processSuccess(state: State, action: Action, item: StationItem) {
        updated = true
        if (action == Action.DELETE) {
            adapter.removeItem(item)
        } else {
            adapter.addItem(item)
        }

        ex.postToUi(Runnable {
            vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
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

    private fun performFavorite(station: Station) {
        request(id = station.id, action = Action.FAVORITE, single = true, input = station)
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        id: String? = Constants.Default.NULL,
        input: Station? = Constants.Default.NULL
    ) {

        val request = StationRequest(
            action = action,
            single = single,
            progress = progress,
            limit = Constants.Limit.Radio.STATIONS,
            input = input,
            id = id,
            countryCode = countryCode
        )
        vm.request(request)
    }

    private val serviceUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updatePlaying()
        }
    }
}