package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentRecyclerBinding
import javax.inject.Inject

/**
 * Created by roman on 2019-08-04
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class ScanFragment @Inject constructor() :
    BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentRecyclerBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    override fun getLayoutId(): Int {
        return R.layout.fragment_recycler
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()

        session.track()
        //  request(progress = true)
        //  initTitleSubtitle()
    }

    override fun onStopUi() {
        //processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onRefresh() {
        super.onRefresh()
        //request(progress = true)
    }

    private fun initView() {
        bind = super.binding as FragmentRecyclerBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bind.fab.setOnClickListener(this)

        // vm = ViewModelProviders.of(this, factory).get(ApkViewModel::class.java)
        // vm.observeUiState(this, Observer { this.processUiState(it) })
        // vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
    }

    private fun initRecycler() {
        /* bind.setItems(ObservableArrayList<Any>())
         adapter = ApkAdapter(this)
         adapter.setStickyHeaders(false)
         scroller = object : OnVerticalScrollListener() {}
         ViewUtil.setRecycler(
             adapter,
             bindRecycler.recycler,
             SmoothScrollGridLayoutManager(context!!, adapter.getSpanCount()),
             FlexibleItemDecoration(context!!)
                 .addItemViewType(R.layout.item_apk, adapter.getItemOffset())
                 .withEdge(true),
             null,
             scroller,
             null
         )*/
    }
}