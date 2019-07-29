package com.dreampany.history.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.MenuTint
import com.dreampany.frame.util.ViewUtil
import com.dreampany.history.R
import com.dreampany.history.data.model.History
import com.dreampany.history.data.model.HistoryRequest
import com.dreampany.history.databinding.ContentRecyclerBinding
import com.dreampany.history.databinding.ContentTopStatusBinding
import com.dreampany.history.databinding.FragmentFavoriteBinding
import com.dreampany.history.ui.activity.ToolsActivity
import com.dreampany.history.ui.adapter.HistoryAdapter
import com.dreampany.history.ui.enums.UiSubtype
import com.dreampany.history.ui.enums.UiType
import com.dreampany.history.ui.model.HistoryItem
import com.dreampany.history.ui.model.UiTask
import com.dreampany.history.vm.HistoryViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by roman on 2019-07-28
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class FavoriteFragment
@Inject constructor() :
    BaseMenuFragment(),
    SmartAdapter.Callback<HistoryItem>,
    HistoryViewModel.OnClickListener {

    private val NONE = "none"
    private val EMPTY = "empty"

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentFavoriteBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var vm: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_favorite
    }

    override fun getMenuId(): Int {
        return R.menu.menu_search
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()
        session.track()
        initTitleSubtitle()
        processUiState(UiState.NONE)
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onResume() {
        super.onResume()
        request(true, true, true)
    }

    override fun onRefresh() {
        super.onRefresh()
        request(true, true, true)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        val searchItem = findMenuItemById(R.id.item_search)
        MenuTint.colorMenuItem(
            searchItem,
            ColorUtil.getColor(context, R.color.material_white),
            null
        )
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        if (position != RecyclerView.NO_POSITION) {
            val item = adapter.getItem(position) as HistoryItem
            openUi(item.item);
            return true
        }
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText)
            adapter.filterItems()
        }
        return false
    }

    override val empty: Boolean
        get() = adapter.isEmpty()
    override val items: List<HistoryItem>?
        get() = adapter.getCurrentItems()
    override val visibleItems: List<HistoryItem>?
        get() = adapter.getVisibleItems()
    override val visibleItem: HistoryItem?
        get() = adapter.getVisibleItem()

    override fun onFavoriteClicked(history: History) {
        vm.toggleFavorite(history)
    }

    override fun onLinkClicked(link: String) {
        openSite(link)
    }

    private fun initTitleSubtitle() {
        setTitle(R.string.favorites)
        val subtitle = getString(R.string.history_format, adapter.itemCount)
        setSubtitle(subtitle)
    }

    private fun initView() {
        bind = super.binding as FragmentFavoriteBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            NONE,
            LayoutInflater.from(context).inflate(R.layout.item_none, null)
        )
        bind.stateful.setStateView(
            EMPTY,
            LayoutInflater.from(context).inflate(R.layout.item_empty, null)
        )

        ViewUtil.setText(this, R.id.text_empty, R.string.empty_favourite)
        ViewUtil.setSwipe(bind.layoutRefresh, this)

        vm = ViewModelProviders.of(this, factory).get(HistoryViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

        vm.setOnLinkClickListener(this)
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = HistoryAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = OnVerticalScrollListener()
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_history, vm.itemOffset)
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun processUiState(state: UiState) {
        when (state) {
            UiState.NONE -> bind.stateful.setState(NONE)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.EMPTY -> bind.stateful.setState(EMPTY)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
            }
        }
    }

    fun processResponse(response: Response<List<HistoryItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<HistoryItem>>
            processSuccess(result.type, result.data)
        }
    }

    private fun processSingleResponse(response: Response<HistoryItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<HistoryItem>
            processSingleSuccess(result.data)
        }
    }

    private fun processProgress(loading: Boolean) {
        if (loading) {
            vm.updateUiState(UiState.SHOW_PROGRESS)
        } else {
            vm.updateUiState(UiState.HIDE_PROGRESS)
        }
    }

    private fun processFailure(error: Throwable) {
        if (error is IOException || error.cause is IOException) {
            vm.updateUiState(UiState.OFFLINE)
        } else if (error is EmptyException) {
            vm.updateUiState(UiState.EMPTY)
        } else if (error is ExtraException) {
            vm.updateUiState(UiState.EXTRA)
        } else if (error is MultiException) {
            for (e in error.errors) {
                processFailure(e)
            }
        }
    }

    private fun processSuccess(type: Response.Type, items: List<HistoryItem>) {
        adapter.addFavoriteItems(items)
        ex.postToUi({ processUiState(UiState.EXTRA) }, 500L)
        initTitleSubtitle()
    }

    private fun processSingleSuccess(item: HistoryItem) {
        adapter.updateSilently(item)
    }

    private fun request(
        important: Boolean,
        progress: Boolean,
        favorite: Boolean
    ) {
        val type = vm.getHistoryType()
        Timber.v("Request type %s", type)
        val day = vm.getDay()
        val month = vm.getMonth()

        val request = HistoryRequest(type, day, month, important, progress, favorite)
        vm.load(request)
    }

    fun openSite(url: String) {
        var task = UiTask<History>(true, UiType.SITE, UiSubtype.VIEW, null, url)
        openActivity(ToolsActivity::class.java, task)
    }

    private fun openUi(history: History) {
        var task = UiTask<History>(false, UiType.HISTORY, UiSubtype.VIEW, history)
        openActivity(ToolsActivity::class.java, task)
    }
}