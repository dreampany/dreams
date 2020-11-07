package com.dreampany.radio.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.FragmentScope
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.radio.databinding.StationsFragmentBinding
import com.dreampany.radio.ui.vm.SearchViewModel
import com.dreampany.stateful.StatefulLayout
import kotlinx.android.synthetic.main.content_recycler.view.*
import timber.log.Timber
import javax.inject.Inject
import com.dreampany.radio.R
import com.dreampany.radio.data.enums.Action
import com.dreampany.radio.data.enums.State
import com.dreampany.radio.data.enums.Subtype
import com.dreampany.radio.data.enums.Type
import com.dreampany.radio.data.model.Page
import com.dreampany.radio.data.source.pref.Prefs
import com.dreampany.radio.manager.RadioPlayerManager
import com.dreampany.radio.misc.Constants
import com.dreampany.radio.ui.adapter.FastStationAdapter
import com.dreampany.radio.ui.model.StationItem
import com.dreampany.radio.ui.vm.StationViewModel

/**
 * Created by roman on 2/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class StationsFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var pref: Prefs

    @Inject
    internal lateinit var player: RadioPlayerManager

    private lateinit var bind: StationsFragmentBinding
    private lateinit var searchVm: SearchViewModel
    private lateinit var vm: StationViewModel
    private lateinit var adapter: FastStationAdapter
    private lateinit var input: Page
    private lateinit var query: String

    override val layoutRes: Int = R.layout.stations_fragment
    override val menuRes: Int = R.menu.stations_menu
    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Page>
        input = task.input ?: return
        initUi()
        initRecycler(state)
        player.bind()
    }

    override fun onStopUi() {
        player.unbind()
    }

    override fun onStart() {
        super.onStart()
        onRefresh()
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

    private fun onItemPressed(view: View, input: StationItem) {
        player.play(input.input)
    }

    private fun loadStations() {
        val order = pref.order
        if (input.type.isLocal) {
            vm.readsLocal(context.countryCode, order, 0)
        } else if (input.type.isCustom) {
            vm.search(input.id, order, 0)
        } else {
            vm.reads(input.type, order, 0)
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

        vm.subscribes(this, { this.processResponse(it) })

        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_stations)
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastStationAdapter({ currentPage: Int ->
            Timber.v("CurrentPage: %d", currentPage)
            onRefresh()
        }, this::onItemPressed)
        adapter.initRecycler(state, bind.layoutRecycler.recycler)
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<StationItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<StationItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
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
        adapter.clearAll()
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
        searchVm.write(query, Constants.Values.STATIONS)
    }
}