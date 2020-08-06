package com.dreampany.tools.ui.home.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.home.Subtype
import com.dreampany.tools.data.enums.home.Type
import com.dreampany.tools.data.model.home.Feature
import com.dreampany.tools.databinding.RecyclerFragmentBinding
import com.dreampany.tools.ui.crypto.activity.CoinsActivity
import com.dreampany.tools.ui.history.activity.HistoriesActivity
import com.dreampany.tools.ui.home.adapter.FastFeatureAdapter
import com.dreampany.tools.ui.home.model.FeatureItem
import com.dreampany.tools.ui.home.vm.FeatureViewModel
import com.dreampany.tools.ui.news.activity.NewsActivity
import com.dreampany.tools.ui.note.activity.NotesActivity
import com.dreampany.tools.ui.radio.activity.StationsActivity
import com.dreampany.tools.ui.wifi.activity.WifisActivity
import kotlinx.android.synthetic.main.content_recycler.view.*
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
    private lateinit var adapter: FastFeatureAdapter
    private lateinit var vm: FeatureViewModel

    override val layoutRes: Int = R.layout.recycler_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        if (adapter.isEmpty)
            vm.loadFeatures()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (!::bind.isInitialized) {
            bind = getBinding()
            vm = createVm(FeatureViewModel::class)
            vm.subscribes(this, Observer { this.processResponse(it) })
        }
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastFeatureAdapter(clickListener = { item: FeatureItem ->
                Timber.v("StationItem: %s", item.item.toString())
                openUi(item.item)
            })

            adapter.initRecycler(state, bind.layoutRecycler.recycler)
        }
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

    private fun processResults(result: List<FeatureItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }
    }

    private fun openUi(item: Feature) {
        when (item.subtype) {
            Subtype.WIFI -> activity.open(WifisActivity::class)
            Subtype.CRYPTO -> activity.open(CoinsActivity::class)
            Subtype.RADIO -> activity.open(StationsActivity::class)
            Subtype.NOTE -> activity.open(NotesActivity::class)
            Subtype.HISTORY -> activity.open(HistoriesActivity::class)
            Subtype.NEWS -> activity.open(NewsActivity::class)
        }
    }
}