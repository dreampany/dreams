package com.dreampany.tools.ui.crypto.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.init
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.CryptoPref
import com.dreampany.tools.databinding.CryptoGraphFragmentBinding
import com.dreampany.tools.databinding.RecyclerFragmentBinding
import com.dreampany.tools.ui.crypto.adapter.FastCoinAdapter
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
import kotlinx.android.synthetic.main.content_recycler_ad.view.*
import timber.log.Timber
import javax.inject.Inject

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
    internal lateinit var cryptoPref: CryptoPref

    private lateinit var bind: CryptoGraphFragmentBinding
    private lateinit var vm: CoinViewModel
    private lateinit var input: Coin

    override val layoutRes: Int = R.layout.crypto_graph_fragment

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Page>
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

        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler,
            cryptoPref.getCurrency(),
            cryptoPref.getSort(),
            cryptoPref.getOrder()
        )
    }

    private fun processResponse(response: Response<CryptoType, CryptoSubtype, CryptoState, CryptoAction, List<CoinItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<CryptoType, CryptoSubtype, CryptoState, CryptoAction, List<CoinItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        showDialogue(
            R.string.title_dialog_features,
            message = error.message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResults(result: List<CoinItem>?) {
        if (result == null) {

        } else {
            adapter.addItems(result)
        }
    }
}