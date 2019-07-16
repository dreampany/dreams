package com.dreampany.cast.ui.fragment

import android.Manifest
import android.os.Bundle
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.cast.R
import com.dreampany.cast.databinding.ContentRecyclerBinding
import com.dreampany.cast.databinding.FragmentHomeBinding
import com.dreampany.cast.ui.adapter.UserAdapter
import com.dreampany.cast.vm.UserViewModel
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.databinding.ContentTopStatusBinding
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.util.ViewUtil
import com.karumi.dexter.MultiplePermissionsReport
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/16/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HomeFragment @Inject constructor() : BaseMenuFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var bindHome: FragmentHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var adapter: UserAdapter
    private lateinit var vm: UserViewModel

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()

        checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onStopUi() {
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        if (report.areAllPermissionsGranted()) {
            vm.startNearby()
        }
    }

    private fun initView() {
        setTitle(R.string.home)
        bindHome = super.binding as FragmentHomeBinding
        bindStatus = bindHome.layoutTopStatus
        // bindRecycler = bindHome.layoutRecycler

        vm = ViewModelProviders.of(this, factory).get(UserViewModel::class.java)
        vm.observeUiState(this, Observer { processUiState(it) })
    }

    private fun initRecycler() {
        bindHome.items = ObservableArrayList<Any>()
        adapter = UserAdapter(this)
        adapter.setStickyHeaders(false)
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_user, vm.itemOffset)
                .withEdge(true),
            null,
            null,
            null
        )
    }

    private fun processUiState(state: UiState) {
        when (state) {
            UiState.SHOW_PROGRESS ->
                if (!bindHome.layoutRefresh.isRefreshing) {
                    bindHome.layoutRefresh.isRefreshing = true
                }
            UiState.HIDE_PROGRESS ->
                if (bindHome.layoutRefresh.isRefreshing) {
                    bindHome.layoutRefresh.isRefreshing = false
                }
            UiState.OFFLINE -> {
            }
            UiState.ONLINE -> {
            }
            UiState.EXTRA -> processUiState(if (adapter.isEmpty) UiState.EMPTY else UiState.CONTENT)
            UiState.SEARCH -> {
            }
            UiState.EMPTY -> {
            }
            UiState.ERROR -> {
            }
            UiState.CONTENT -> {
            }
        }// bindStatus.layoutExpandable.expand();
        //   bindStatus.layoutExpandable.collapse();
        //binding.stateful.setState(SEARCH);
        //   binding.stateful.setState(SEARCH);
        //  binding.stateful.setState(StatefulLayout.State.CONTENT);
    }
}