package com.dreampany.crypto.ui.home.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.data.source.pref.AppPref
import com.dreampany.crypto.databinding.RecyclerFragmentBinding
import com.dreampany.crypto.ui.home.adapter.FastTickerAdapter
import com.dreampany.crypto.ui.home.model.TickerItem
import com.dreampany.crypto.ui.home.vm.TickerViewModel
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.init
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class TickersFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var pref: AppPref

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var adapter: FastTickerAdapter
    private lateinit var vm: TickerViewModel

    private lateinit var input: Coin

    override val layoutRes: Int = R.layout.recycler_fragment

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Coin>
        input = task.input ?: return
        initUi()
        initRecycler(state)
        onRefresh()
    }

    override fun onStopUi() {
        adapter.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRefresh() {
        if (adapter.isEmpty) {
            loadTickers()
            return
        }
        bind.swipe.refresh(false)
    }

    private fun loadTickers() {
        if (::input.isInitialized) {
            vm.loadTickers(input.slug)
        }
    }

    private fun initUi() {
        bind = binding()
        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_tickers)
        vm = createVm(TickerViewModel::class)
        vm.subscribes(this, Observer { this.processResponses(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastTickerAdapter()
        }

        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler
        )
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<TickerItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<TickerItem>>) {
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

    private fun processResults(result: List<TickerItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

}