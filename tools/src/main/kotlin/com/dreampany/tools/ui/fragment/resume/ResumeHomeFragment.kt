package com.dreampany.tools.ui.fragment.resume

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.extension.toTint
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.MenuTint
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentRecyclerBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.ResumeAdapter
import com.dreampany.tools.ui.misc.ResumeRequest
import com.dreampany.tools.ui.model.ResumeItem
import com.dreampany.tools.ui.vm.resume.ResumeViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class ResumeHomeFragment
@Inject constructor() :
    BaseMenuFragment(),
    SmartAdapter.OnUiItemClickListener<ResumeItem, Action> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentRecyclerBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var adapter: ResumeAdapter
    private lateinit var vm: ResumeViewModel
    private lateinit var scroller: OnVerticalScrollListener

    override fun getLayoutId(): Int {
        return R.layout.fragment_recycler
    }

    override fun getMenuId(): Int {
        return R.menu.menu_search
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_resume
    }

    override fun getScreen(): String {
        return Constants.resumeHome(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        getSearchMenuItem().toTint(context, R.color.material_white)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        session.track()
        request(progress = true)
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.RequestCode.ADD,
            Constants.RequestCode.EDIT,
            Constants.RequestCode.FAVORITE -> {
                if (isOkay(resultCode)) {
                    data?.run {
                        val task = getCurrentTask<UiTask<Resume>>(this)
                        task?.run {
                            if (state == State.ADDED || state == State.EDITED) {
                                ex.postToUi(Runnable {
                                    request(
                                        state = State.UI,
                                        action = Action.GET,
                                        progress = true,
                                        input = this.input
                                    )
                                }, 500L)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        if (adapter.isEmpty) {
            request(progress = true)
        } else {
            vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        }
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (adapter.isEmpty) return false
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText)
            adapter.filterItems()
        }
        return false
    }

    override fun onUiItemClick(view: View, item: ResumeItem, action: Action) {
        when (action) {
            Action.PREVIEW -> {
                openPreviewUi(item.item)
            }
            Action.EDIT -> {
                openEditUi(item.item)
            }
        }

    }

    override fun onUiItemLongClick(view: View, item: ResumeItem, action: Action) {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.layout_empty,
            R.id.fab -> {
                openAddUi()
            }
        }
    }

    private fun initUi() {
        bind = super.binding as FragmentRecyclerBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty_resume, null).apply {
                setOnClickListener(this@ResumeHomeFragment)
            }
        )

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bind.fab.show()
        bind.fab.setImageResource(R.drawable.ic_add_black_24dp)
        bind.fab.setOnClickListener(this)

        vm = ViewModelProvider(this, factory).get(ResumeViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
        vm.updateUiState(uiState = UiState.DEFAULT)
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = ResumeAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {}
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_resume, adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun processUiState(response: Response.UiResponse) {
        Timber.v("UiState %s", response.uiState.name)
        when (response.uiState) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
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
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
            }
        }
    }

    fun processMultipleResponse(response: Response<List<ResumeItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(state = result.state, action =  result.action, loading =  result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state =  result.state,  action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<ResumeItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    fun processSingleResponse(response: Response<ResumeItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(state = result.state, action =  result.action, loading =  result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state =  result.state,  action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<ResumeItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<ResumeItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        ex.postToUi(Runnable {
            vm.updateUiState(state = state,action =  action,uiState =  UiState.EXTRA)
        }, 500L)
    }

    private fun processSuccess(state: State, action: Action, item: ResumeItem) {
        if (action == Action.DELETE) {
            adapter.removeItem(item)
        } else {
            adapter.addItem(item)
        }

        ex.postToUi(Runnable {
            vm.updateUiState(state = state,action =  action, uiState = UiState.EXTRA)
        }, 500L)
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        input: Resume? = Constants.Default.NULL,
        id: String? = Constants.Default.NULL
    ) {
        val request = ResumeRequest(
            state = state,
            action = action,
            single = single,
            progress = progress,
            input = input,
            id = id
        )
        vm.request(request)
    }

    private fun openAddUi() {
        val task = UiTask<Resume>(
            type = Type.RESUME,
            action = Action.ADD
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.ADD)
    }

    private fun openEditUi(resume: Resume) {
        val task = UiTask<Resume>(
            type = Type.RESUME,
            action = Action.EDIT,
            input = resume
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.EDIT)
    }

    private fun openPreviewUi(resume: Resume) {
        val task = UiTask<Resume>(
            type = Type.RESUME,
            action = Action.PREVIEW,
            input = resume
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.PREVIEW)
    }
}