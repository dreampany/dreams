package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.ui.misc.FeatureRequest
import com.dreampany.tools.data.model.Feature
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentHomeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.FeatureAdapter
import com.dreampany.tools.ui.model.FeatureItem
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.ui.vm.FeatureViewModel
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
class HomeFragment
@Inject constructor(
) : BaseMenuFragment(), SmartAdapter.OnUiItemClickListener<FeatureItem?, Any?> {

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

    override fun getScreen(): String {
        return Constants.home(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()
        initTitleSubtitle()

        session.track()
        request(progress = true)
    }

    override fun onStopUi() {

    }

    override fun onUiItemClick(view: View, item: FeatureItem?, action: Any?) {
        item?.run {
            Timber.v("%s", this.item.type.name)
            openUi(this)
        }

    }

    override fun onUiItemLongClick(view: View, item: FeatureItem?, action: Any?) {
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
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
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
            type = Type.DEFAULT,
            action = Action.GET,
            important = important,
            progress = progress
        )
        vm.load(request)
    }

    private fun processUiState(response: Response.UiResponse) {
        Timber.v("UiState %s", response.uiState.name)
        when (response.uiState) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> {
                response.uiState = if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT
                processUiState(response)
            }
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    fun processMultipleResponse(response: Response<List<FeatureItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<FeatureItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<FeatureItem>) {
        adapter.setItems(items)
        ex.postToUi(Runnable {
            vm.updateUiState(state, action, UiState.EXTRA)
        }, 500L)
    }


    private fun openUi(uiItem: FeatureItem) {
        var task: UiTask<Feature> = UiTask<Feature>(
            type = uiItem.item.type,
            subtype = Subtype.DEFAULT,
            state = State.HOME,
            action = Action.OPEN,
            input = uiItem.item
        )
        openActivity(ToolsActivity::class.java, task)
    }
}