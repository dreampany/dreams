package com.dreampany.tools.ui.radio.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.dreampany.common.data.model.Response
import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.common.misc.extension.countryCode
import com.dreampany.common.misc.extension.task
import com.dreampany.common.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.RadioSubtype
import com.dreampany.tools.data.enums.radio.RadioType
import com.dreampany.tools.databinding.RecyclerFragmentBinding
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

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: StationViewModel

    private lateinit var adapter: FastStationAdapter

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        loadStations()
    }

    override fun onStopUi() {
    }

    private fun loadStations() {
        val task = task ?: return
        if (task.state is RadioState) {
            when (task.state) {
                RadioState.LOCAL -> {
                    vm.loadStations(task.state as RadioState, context.countryCode, adapter.itemCount)
                }
                RadioState.TRENDS,
                RadioState.POPULAR -> {
                    vm.loadStations(task.state as RadioState, adapter.itemCount)
                }
            }
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