package com.dreampany.crypto.ui.home.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.data.source.pref.AppPref
import com.dreampany.crypto.databinding.RecyclerFragmentBinding
import com.dreampany.crypto.ui.home.adapter.FastCoinAdapter
import com.dreampany.crypto.ui.home.model.CoinItem
import com.dreampany.crypto.ui.home.vm.CoinViewModel
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.extension.refresh
import com.dreampany.framework.misc.extension.task
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import timber.log.Timber
import javax.inject.Inject
import com.dreampany.crypto.R
import com.dreampany.framework.misc.extension.init

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class GraphFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var cryptoPref: AppPref

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: CoinViewModel
    private lateinit var adapter: FastCoinAdapter
    private lateinit var input: Coin

    override val layoutRes: Int = R.layout.recycler_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        val task: UiTask<Type, Subtype, State, Action, Coin> =
            (task ?: return) as UiTask<Type, Subtype, State, Action, Coin>
        input = task.input ?: return
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
        loadCoins()
    }

    private fun loadCoins() {
        if (::input.isInitialized)
            vm.loadCoin(input.id)
    }

    private fun initUi() {
        bind = getBinding()
        bind.swipe.init(this)
        vm = createVm(CoinViewModel::class)
        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastCoinAdapter()
        }

        /*adapter.initRecycler(
            state,
            bind.layoutRecycler.,
            cryptoPref.getCurrency(),
            cryptoPref.getSort(),
            cryptoPref.getOrder()
        )*/
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<CoinItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<CoinItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        /*showDialogue(
            R.string.title_dialog_features,
            message = error.message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )*/
    }

    private fun processResults(result: List<CoinItem>?) {
        if (result == null) {

        } else {
            adapter.addItems(result)
        }
    }
}