package com.dreampany.tools.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.data.model.Response
import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.common.misc.extension.open
import com.dreampany.common.misc.func.OnVerticalScrollListener
import com.dreampany.common.ui.adapter.BaseAdapter
import com.dreampany.common.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.home.Subtype
import com.dreampany.tools.data.enums.home.Type
import com.dreampany.tools.databinding.HomeFragmentBinding
import com.dreampany.tools.ui.crypto.activity.CoinsActivity
import com.dreampany.tools.ui.home.adapter.FeatureAdapter
import com.dreampany.tools.ui.home.vm.FeatureViewModel
import com.dreampany.tools.data.model.home.Feature
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
@Inject constructor() : InjectFragment(), BaseAdapter.OnItemClickListener<Feature> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var bind: HomeFragmentBinding
    private lateinit var vm: FeatureViewModel

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var featureAdapter: FeatureAdapter

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.home_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        vm.loadFeatures()
    }

    override fun onStopUi() {
    }

    override fun onItemClick(item: Feature) {
        openUi(item)
    }

    override fun onChildItemClick(view: View, item: Feature) {
    }

    private fun initUi() {
        bind = getBinding()
        vm = ViewModelProvider(this, factory).get(FeatureViewModel::class.java)

        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler() {
        scroller = object : OnVerticalScrollListener() {
            override fun onScrolledToBottom() {

            }
        }

        featureAdapter = FeatureAdapter(this)

        val recyclerLayout = GridLayoutManager(context, 3)
        recyclerLayout.orientation = RecyclerView.VERTICAL
        recyclerLayout.isSmoothScrollbarEnabled = true

        bind.recycler.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            itemAnimator = DefaultItemAnimator()
            addOnScrollListener(scroller)
            adapter = featureAdapter
        }
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<Feature>>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<Feature>>) {
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

    private fun processResults(features: List<Feature>) {
        featureAdapter.addAll(features, true)
    }

    private fun openUi(item: Feature) {
        when (item.subtype) {
            Subtype.CRYPTO -> activity.open(CoinsActivity::class)
        }
        when (item.subtype) {
            Subtype.RADIO -> activity.open(StationsActivity::class)
        }

    }
}