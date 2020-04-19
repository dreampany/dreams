package com.dreampany.tools.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.common.data.model.Response
import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.common.misc.extension.open
import com.dreampany.common.ui.adapter.BaseAdapter
import com.dreampany.common.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.home.Subtype
import com.dreampany.tools.data.enums.home.Type
import com.dreampany.tools.data.model.home.Feature
import com.dreampany.tools.databinding.RecyclerFragmentBinding
import com.dreampany.tools.ui.crypto.activity.CoinsActivity
import com.dreampany.tools.ui.home.adapter.FastFeatureAdapter
import com.dreampany.tools.ui.home.model.FeatureItem
import com.dreampany.tools.ui.home.vm.FeatureViewModel
import com.dreampany.tools.ui.radio.activity.StationsActivity
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HomeFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: FeatureViewModel

    private lateinit var adapter: FastFeatureAdapter

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.recycler_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        vm.loadFeatures()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = getBinding()
        vm = createVm(FeatureViewModel::class)

        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastFeatureAdapter(clickListener = { item: FeatureItem ->
                Timber.v("StationItem: %s", item.item.toString())
                openUi(item.item)
            })
        }

        adapter.initRecycler(
            state,
            bind.recycler
        )
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<FeatureItem>>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<FeatureItem>>) {
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

    private fun processResults(result: List<FeatureItem>) {
        adapter.addItems(result)
    }

    private fun openUi(item: Feature) {
        when (item.subtype) {
            Subtype.CRYPTO -> activity.open(CoinsActivity::class)
            Subtype.RADIO -> activity.open(StationsActivity::class)
        }
    }
}