package com.dreampany.tools.ui.radio.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.stateful.StatefulLayout
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.radio.RadioAction
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.RadioSubtype
import com.dreampany.tools.data.enums.radio.RadioType
import com.dreampany.tools.databinding.RecyclerChildFragmentBinding
import com.dreampany.tools.manager.RadioPlayerManager
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.ui.misc.vm.SearchViewModel
import com.dreampany.tools.ui.radio.adapter.FastStationAdapter
import com.dreampany.tools.ui.radio.model.StationItem
import com.dreampany.tools.ui.radio.vm.StationViewModel
import kotlinx.android.synthetic.main.content_recycler.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class StationsFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var player: RadioPlayerManager

    private lateinit var bind: RecyclerChildFragmentBinding
    private lateinit var searchVm: SearchViewModel
    private lateinit var vm: StationViewModel
    private lateinit var adapter: FastStationAdapter
    private lateinit var query: String

    override val layoutRes: Int = R.layout.recycler_child_fragment
    override val menuRes: Int = R.menu.menu_stations
    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        onRefresh()
        player.bind()
    }

    override fun onStopUi() {
        player.unbind()
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
        loadStations()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        val value = newText.trimValue
        if (value.isNotEmpty()) {
            this.query = value
            ex.getUiHandler().removeCallbacks(runner)
            ex.getUiHandler().postDelayed(runner, 3000L)
        }
        return false
    }
    private val serviceUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updatePlaying()
        }
    }

    private fun onStationClicked(item: StationItem) {
        activityCallback?.onItem(item)
        player.play(item.input)
    }

    private fun loadStations() {
        val task = task ?: return
        if (task.state is RadioState) {
            when (task.state) {
                RadioState.LOCAL -> {
                    vm.loadStations(
                        task.state as RadioState,
                        context.countryCode,
                        adapter.itemCount.toLong()
                    )
                }
                RadioState.TRENDS,
                RadioState.POPULAR -> {
                    vm.loadStations(task.state as RadioState, adapter.itemCount.toLong())
                }
            }
        }
    }

    private fun updatePlaying() {
        if (player.isPlaying()) {
            player.getStation()?.run {
                /*mapper.getUiItem(this.id)?.run {
                    adapter.setSelection(this, true)
                }*/
            }
        } else {
            //adapter.clearSelection()
        }
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        searchVm = createVm(SearchViewModel::class)
        vm = createVm(StationViewModel::class)

        vm.subscribes(this, Observer { this.processResponse(it) })

        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_stations)
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastStationAdapter(scrollListener = { currentPage: Int ->
            Timber.v("CurrentPage: %d", currentPage)
            onRefresh()
        }, clickListener = { item: StationItem ->
            Timber.v("StationItem: %s", item.input.toString())
            onStationClicked(item)
        })
        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler
        )
    }

    private fun processResponse(response: Response<RadioType, RadioSubtype, RadioState, RadioAction, List<StationItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<RadioType, RadioSubtype, RadioState, RadioAction, List<StationItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
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

    private fun processResults(result: List<StationItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private val runner = Runnable {
        writeSearch()
    }

    private fun writeSearch() {
        if (isFinishing) return
        searchVm.write(query, Constants.Values.Radio.STATIONS)
    }
}