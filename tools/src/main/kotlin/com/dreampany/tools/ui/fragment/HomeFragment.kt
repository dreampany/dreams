package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.FeatureType
import com.dreampany.tools.data.misc.FeatureRequest
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentHomeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.FeatureAdapter
import com.dreampany.tools.ui.enums.UiSubtype
import com.dreampany.tools.ui.enums.UiType
import com.dreampany.tools.ui.model.FeatureItem
import com.dreampany.tools.ui.model.UiTask
import com.dreampany.tools.vm.FeatureViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HomeFragment @Inject constructor() :
    BaseMenuFragment(), SmartAdapter.OnClickListener<FeatureItem?, Any?> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var vm: FeatureViewModel
    private lateinit var adapter: FeatureAdapter
    private lateinit var scroller: OnVerticalScrollListener

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }
/*
    override fun getMenuId(): Int {
        return R.menu.menu_home
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }*/

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()

        session.track()
        request(progress = true)
        initTitleSubtitle()
    }

    override fun onStopUi() {

    }

    override fun onClick(item: FeatureItem?, action: Any?) {
        item?.run {
            Timber.v("%s", this.item.type.name)
            openUi(this)
        }

    }

    override fun onLongClick(item: FeatureItem?, action: Any?) {
        item?.run {
            Timber.v("%s", this.item.type.name)
        }

    }

    private fun initTitleSubtitle() {
        setTitle(R.string.home)
    }

    private fun initView() {

        bind = super.binding as FragmentHomeBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.NONE.name,
            LayoutInflater.from(context).inflate(R.layout.item_none, null)
        )

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bind.fab.setOnClickListener(this)

        vm = ViewModelProviders.of(this, factory).get(FeatureViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = FeatureAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {}
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollGridLayoutManager(context!!, adapter.getSpanCount()),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_feature, adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun request(
        important: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val request = FeatureRequest(
            type = FeatureType.DEFAULT,
            important = important,
            progress = progress
        )
        vm.load(request)
    }

    private fun processUiState(state: UiState) {
        when (state) {
            UiState.NONE -> bind.stateful.setState(UiState.NONE.name)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    fun processMultipleResponse(response: Response<List<FeatureItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<FeatureItem>>
            processSuccess(result.type, result.data)
        }
    }

    private fun processSuccess(type: Response.Type, items: List<FeatureItem>) {
        Timber.v("Result Type[%s] Size[%s]", type.name, items.size)
        adapter.setItems(items)
        ex.postToUi({ processUiState(UiState.EXTRA) }, 500L)
    }


    private fun openUi(uiItem: FeatureItem) {
        var task: UiTask<Base>? = null
        when (uiItem.item.type) {
            FeatureType.APK -> {
                task = UiTask<Base>(false, UiType.APK, UiSubtype.VIEW, uiItem.item)
            }
            FeatureType.SCAN -> {
                task = UiTask<Base>(false, UiType.SCAN, UiSubtype.VIEW, uiItem.item)
            }
            FeatureType.NOTE -> {
                task = UiTask<Base>(false, UiType.NOTE, UiSubtype.VIEW, uiItem.item)
            }
        }
        task?.run {
            openActivity(ToolsActivity::class.java, this)
        }
    }
}