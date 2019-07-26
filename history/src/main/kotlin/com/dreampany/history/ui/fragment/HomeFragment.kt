package com.dreampany.history.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.DatePicker
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.callback.SearchViewCallback
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.util.*
import com.dreampany.history.R
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.model.HistoryRequest
import com.dreampany.history.databinding.ContentRecyclerBinding
import com.dreampany.history.databinding.ContentTopStatusBinding
import com.dreampany.history.databinding.FragmentHomeBinding
import com.dreampany.history.ui.adapter.HistoryAdapter
import com.dreampany.history.ui.model.HistoryItem
import com.dreampany.history.vm.HistoryViewModel
import com.dreampany.language.Language
import com.dreampany.language.LanguagePicker
import com.miguelcatalan.materialsearchview.MaterialSearchView
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HomeFragment
@Inject constructor() :
    BaseMenuFragment(),
    MaterialSearchView.OnQueryTextListener,
    MaterialSearchView.SearchViewListener,
    DatePickerDialog.OnDateSetListener {

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

    override fun getMenuId(): Int {
        return R.menu.menu_home
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()
        request(true, true, false)
    }

    override fun onStopUi() {
        //processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)
        val activity = getParent()

        if (activity is SearchViewCallback) {
            val searchCallback = activity as SearchViewCallback?
            searchView = searchCallback!!.searchView
            val searchItem = getSearchMenuItem()
            initSearchView(searchView, searchItem)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_search ->
                //searchView.open(item);
                return true
            R.id.item_date -> {
                openDatePicker()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSearchViewClosed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSearchViewShown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
    }


    private fun initSearchView(searchView: MaterialSearchView, searchItem: MenuItem?) {
        MenuTint.colorMenuItem(
            searchItem,
            ColorUtil.getColor(context, R.color.material_white),
            null
        )
        searchView.setMenuItem(searchItem)
        searchView.setSubmitOnClick(true)

        searchView.setOnSearchViewListener(this)
        searchView.setOnQueryTextListener(this)
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



        vm = ViewModelProviders.of(this, factory).get(HistoryViewModel::class.java)
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

    private fun openDatePicker() {
        val day = vm.getDay()
        val month = vm.getMonth()
        val year = vm.getYear()
        val picker = ViewUtilKt.createDatePicker(context!!, this, day, month, year)
        picker.show()
    }

    private fun processUiState(state: UiState) {
        when (state) {
            UiState.NONE -> bindHome.stateful.setState(NONE)
            UiState.SHOW_PROGRESS -> if (!bindHome.layoutRefresh.isRefreshing()) {
                bindHome.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bindHome.layoutRefresh.isRefreshing()) {
                bindHome.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.SEARCH -> bindHome.stateful.setState(SEARCH)
            UiState.EMPTY -> bindHome.stateful.setState(SEARCH)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> bindHome.stateful.setState(StatefulLayout.State.CONTENT)
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
        Timber.v("Result Type[%s] Size[%s]", type.name, items.size)
        adapter.setItems(items)
        ex.postToUi({ processUiState(UiState.EXTRA) }, 500L)
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
}