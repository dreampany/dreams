package com.dreampany.history.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModelProvider
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.util.ViewUtil
import com.dreampany.history.R
import com.dreampany.history.databinding.ContentRecyclerBinding
import com.dreampany.history.databinding.ContentTopStatusBinding
import com.dreampany.history.databinding.FragmentHomeBinding
import com.dreampany.history.ui.adapter.HistoryAdapter
import com.dreampany.history.vm.HistoryViewModel
import com.miguelcatalan.materialsearchview.MaterialSearchView
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HomeFragment @Inject constructor() : BaseMenuFragment() {

    private val NONE = "none"
    private val SEARCH = "search"
    private val EMPTY = "empty"

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    private lateinit var bindHome: FragmentHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var searchView: MaterialSearchView

    private lateinit var vm: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()
    }

    override fun onStopUi() {
        //processUiState(UiState.HIDE_PROGRESS)
    }

    private fun initView() {
        setTitle(R.string.home)
        bindHome = super.binding as FragmentHomeBinding
        bindStatus = bindHome.layoutTopStatus
        bindRecycler = bindHome.layoutRecycler

        bindHome.stateful.setStateView(
            NONE,
            LayoutInflater.from(context).inflate(R.layout.item_none, null)
        )

        ViewUtil.setSwipe(bindHome.layoutRefresh, this)
        bindHome.fab.setOnClickListener(this)
    }

    private fun initRecycler() {
        bindHome.setItems(ObservableArrayList<Any>())
        adapter = HistoryAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {
            override fun onScrollingAtEnd() {

            }
        }
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                //.addItemViewType(R.layout.item_word, vm.itemOffset)
                .withEdge(true),
            null,
            scroller, null
        )
    }


}