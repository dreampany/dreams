package com.dreampany.tools.ui.crypto.fragment

import android.os.Bundle
import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.common.misc.extension.init
import com.dreampany.common.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.databinding.RecyclerFragmentBinding
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
import com.dreampany.tools.ui.radio.adapter.FastStationAdapter
import com.dreampany.tools.ui.radio.model.StationItem
import com.dreampany.tools.ui.radio.vm.StationViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class CoinInfoFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: CoinViewModel

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.recycler_fragment

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = getBinding()
        bind.swipe.init(this)
        vm = createVm(CoinViewModel::class)
        //vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler(state: Bundle?) {
        /*if (!::adapter.isInitialized) {
            adapter = FastStationAdapter(scrollListener = { currentPage: Int ->
                Timber.v("CurrentPage: %d", currentPage)
                onRefresh()
            }, clickListener = { item: StationItem ->
                Timber.v("StationItem: %s", item.item.toString())
                onStationClicked(item)
            })
        }

        adapter.initRecycler(
            state,
            bind.recycler
        )*/
    }
}