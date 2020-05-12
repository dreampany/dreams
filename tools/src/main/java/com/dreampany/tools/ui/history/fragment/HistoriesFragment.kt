package com.dreampany.tools.ui.history.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.extension.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.history.HistoryAction
import com.dreampany.tools.data.enums.history.HistoryState
import com.dreampany.tools.data.enums.history.HistorySubtype
import com.dreampany.tools.data.enums.history.HistoryType
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.radio.RadioState
import com.dreampany.tools.data.enums.radio.RadioSubtype
import com.dreampany.tools.data.enums.radio.RadioType
import com.dreampany.tools.databinding.RecyclerFragmentBinding
import com.dreampany.tools.ui.history.adapter.FastHistoryAdapter
import com.dreampany.tools.ui.history.model.HistoryItem
import com.dreampany.tools.ui.history.vm.HistoryViewModel
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
class HistoriesFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: HistoryViewModel

    private lateinit var adapter: FastHistoryAdapter

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.recycler_child_fragment

    override fun menuRes(): Int = R.menu.menu_histories

    override fun searchMenuItemId(): Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        onRefresh()
    }

    override fun onStopUi() {
    }

    override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(context, R.color.material_white)
    }

    override fun onRefresh() {
        loadHistories()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        return false
    }

    private fun loadHistories() {
        val task = task ?: return
        if (task.state is HistoryState) {
            vm.loadHistories(task.state as HistoryState, currentMonth(), currentDay())
        }
    }

    private fun initUi() {
        bind = getBinding()
        bind.swipe.init(this)
        vm = createVm(HistoryViewModel::class)
        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastHistoryAdapter(clickListener = { item: HistoryItem ->

            })
        }

        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler
        )
    }

    private fun processResponse(response: Response<HistoryType, HistorySubtype, HistoryState, HistoryAction, List<HistoryItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<HistoryType, HistorySubtype, HistoryState, HistoryAction, List<HistoryItem>>) {
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

    private fun processResults(result: List<HistoryItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }
    }
}