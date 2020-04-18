package com.dreampany.tools.ui.radio.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import com.dreampany.common.data.model.Response
import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.common.misc.extension.countryCode
import com.dreampany.common.misc.extension.task
import com.dreampany.common.misc.extension.toTint
import com.dreampany.common.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.RadioSubtype
import com.dreampany.tools.data.enums.radio.RadioType
import com.dreampany.tools.databinding.RecyclerFragmentBinding
import com.dreampany.tools.manager.RadioPlayerManager
import com.dreampany.tools.misc.constant.AppConstants
import com.dreampany.tools.ui.radio.adapter.FastStationAdapter
import com.dreampany.tools.ui.radio.model.StationItem
import com.dreampany.tools.ui.radio.vm.StationViewModel
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

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: StationViewModel

    private lateinit var adapter: FastStationAdapter

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.recycler_fragment

    override fun menuRes(): Int = R.menu.menu_stations

    override fun searchMenuItemId(): Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        loadStations()
        player.bind()
    }

    override fun onStopUi() {
        player.debind()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(AppConstants.Service.PLAYER_SERVICE_UPDATE)
        bindLocalCast(serviceUpdateReceiver, filter)
    }

    override fun onPause() {
        debindLocalCast(serviceUpdateReceiver)
        if (!player.isPlaying()) {
            player.destroy()
        }
        super.onPause()
    }

    override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(context, R.color.material_white)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        return false
    }

    private val serviceUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updatePlaying()
        }
    }

    private fun loadStations() {
        val task = task ?: return
        if (task.state is RadioState) {
            when (task.state) {
                RadioState.LOCAL -> {
                    vm.loadStations(
                        task.state as RadioState,
                        context.countryCode,
                        adapter.itemCount
                    )
                }
                RadioState.TRENDS,
                RadioState.POPULAR -> {
                    vm.loadStations(task.state as RadioState, adapter.itemCount)
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
        bind = getBinding()
        vm = createVm(StationViewModel::class)
        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastStationAdapter(scrollListener = { currentPage: Int ->
                Timber.v("CurrentPage: %d", currentPage)
                loadStations()
            }, clickListener = { item: StationItem ->
                Timber.v("StationItem: %s", item.item.toString())
                player.play(item.item)
            })
        }

        adapter.initRecycler(
            state,
            bind.recycler
        )
    }

    private fun processResponse(response: Response<RadioType, RadioSubtype, State, Action, List<StationItem>>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<RadioType, RadioSubtype, State, Action, List<StationItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: Throwable) {
        showDialogue(
            R.string.title_dialog_features,
            message = error.message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResults(coins: List<StationItem>) {
        adapter.addItems(coins)
    }
}