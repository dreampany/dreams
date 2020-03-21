package com.dreampany.tools.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.data.model.Response
import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.common.misc.extension.open
import com.dreampany.common.ui.adapter.BaseAdapter
import com.dreampany.common.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.databinding.HomeFragmentBinding
import com.dreampany.tools.ui.crypto.CryptoActivity
import com.dreampany.tools.ui.home.adapter.FeatureAdapter
import com.dreampany.tools.ui.home.vm.FeatureViewModel
import com.dreampany.tools.ui.home.model.FeatureItem
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

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var bind: HomeFragmentBinding
    private lateinit var vm: FeatureViewModel

    //private lateinit var scroller: OnVerticalScrollListener
    private lateinit var featureAdapter: FeatureAdapter

    override fun layoutId(): Int = R.layout.home_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        vm.loadFeatures()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = getBinding()
        vm = ViewModelProvider(this, factory).get(FeatureViewModel::class.java)

        vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler() {
        featureAdapter = FeatureAdapter(object : BaseAdapter.OnItemClickListener<FeatureItem> {
            override fun onItemClick(item: FeatureItem) {
                openUi(item)
            }

            override fun onChildItemClick(view: View, item: FeatureItem) {
            }
        })

        val recyclerLayout = GridLayoutManager(context, 3)
        recyclerLayout.orientation = RecyclerView.VERTICAL
        recyclerLayout.isSmoothScrollbarEnabled = true

        bind.recycler.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            itemAnimator = DefaultItemAnimator()
            adapter = featureAdapter
            //addOnScrollListener(scroller)
        }
    }

    private fun processResponse(response: Response<List<FeatureItem>, Type, Subtype>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<List<FeatureItem>, Type, Subtype>) {
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

    private fun processResults(features: List<FeatureItem>) {
        featureAdapter.addAll(features, true)
        Timber.v("")
        //goToHomeScreen()
        /*showDialogue(
            R.string.title_dialog_registration,
            message = user.name,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )*/
    }

    private fun openUi(item: FeatureItem) {
        when (item.subtype) {
            Subtype.CRYPTO -> activity.open(CryptoActivity::class)
        }

    }
}