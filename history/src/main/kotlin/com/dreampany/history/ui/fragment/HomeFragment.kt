package com.dreampany.history.ui.fragment

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.api.session.SessionManager
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
import com.dreampany.history.data.model.History
import com.dreampany.history.data.model.HistoryRequest
import com.dreampany.history.databinding.ContentRecyclerBinding
import com.dreampany.history.databinding.ContentTopStatusBinding
import com.dreampany.history.databinding.FragmentHomeBinding
import com.dreampany.history.misc.Constants
import com.dreampany.history.ui.activity.ToolsActivity
import com.dreampany.history.ui.adapter.HistoryAdapter
import com.dreampany.history.ui.enums.UiSubtype
import com.dreampany.history.ui.enums.UiType
import com.dreampany.history.ui.model.HistoryItem
import com.dreampany.history.ui.model.UiTask
import com.dreampany.history.vm.HistoryViewModel
import com.dreampany.language.Language
import com.dreampany.language.LanguagePicker
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
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
    OnMenuItemClickListener<PowerMenuItem>,
    DatePickerDialog.OnDateSetListener,
    HistoryViewModel.OnClickListener {

    private val NONE = "none"
    private val SEARCH = "search"
    private val EMPTY = "empty"

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bindHome: FragmentHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var searchView: MaterialSearchView

    private lateinit var vm: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    private val typeItems = mutableListOf<PowerMenuItem>()
    private var powerMenu: PowerMenu? = null

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

        session.track()
        initTitleSubtitle()
        request(true, true, false)
    }

    override fun onStopUi() {
        vm.setOnLinkClickListener(null)
        processUiState(UiState.HIDE_PROGRESS)
        if (searchView.isSearchOpen()) {
            searchView.closeSearch()
        }
        powerMenu?.run {
            if (isShowing) {
                dismiss()
            }
        }
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)
        val typeItem = findMenuItemById(R.id.item_type)
        val dateItem = findMenuItemById(R.id.item_date)
        val searchItem = getSearchMenuItem()
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context, R.color.material_white),
            null, typeItem, dateItem, searchItem
        )
        val activity = getParent()
        if (activity is SearchViewCallback) {
            val searchCallback = activity as SearchViewCallback?
            searchView = searchCallback!!.searchView
            initSearchView(searchView, searchItem)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_type -> {
                val toolbar = getParent()?.getToolbarRef()
                //val view = findViewById<View>(R.id.item_type)
                toolbar?.run {
                    openTypePicker(this)
                }
                return true
            }
            R.id.item_date -> {
                openDatePicker()
                return true
            }
            R.id.item_search ->
                //searchView.open(item);
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun hasBackPressed(): Boolean {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch()
            return true
        }
        return super.hasBackPressed()
    }

    override fun onRefresh() {
        super.onRefresh()
        request(true, true, false)
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText)
            adapter.filterItems()
        }
        return false
    }

    override fun onSearchViewClosed() {
    }

    override fun onSearchViewShown() {
    }

    override fun onItemClick(position: Int, item: PowerMenuItem) {
        powerMenu?.setSelectedPosition(position)
        powerMenu?.dismiss()
        vm.setHistoryType(item.tag as HistoryType)
        initTitleSubtitle()
        request(true, true, false)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        vm.setDay(dayOfMonth)
        vm.setMonth(month)
        initTitleSubtitle()
        request(true, true, false)
    }

    override fun onFavoriteClicked(history: History) {
        vm.toggleFavorite(history)
     }

    override fun onLinkClicked(link: String) {
        openSite(link)
    }

    private fun initSearchView(searchView: MaterialSearchView, searchItem: MenuItem?) {
        searchView.setMenuItem(searchItem)
        searchView.setSubmitOnClick(true)

        searchView.setOnSearchViewListener(this)
        searchView.setOnQueryTextListener(this)
    }

    private fun initTitleSubtitle() {
        setTitle(TimeUtilKt.getDate(vm.getDay(), vm.getMonth(), Constants.Date.MONTH_DAY))
        val type = vm.getHistoryType().toTitle()
        val subtitle = getString(R.string.type_format, type, adapter.itemCount)
        setSubtitle(subtitle)
    }

    private fun initView() {

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
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

        vm.setOnLinkClickListener(this)

        if (typeItems.isEmpty()) {

            typeItems.add(
                PowerMenuItem(
                    HistoryType.EVENT.toTitle(),
                    HistoryType.EVENT == vm.getHistoryType(),
                    HistoryType.EVENT
                )
            )
            typeItems.add(
                PowerMenuItem(
                    HistoryType.BIRTH.toTitle(),
                    HistoryType.BIRTH == vm.getHistoryType(),
                    HistoryType.BIRTH
                )
            )
            typeItems.add(
                PowerMenuItem(
                    HistoryType.DEATH.toTitle(),
                    HistoryType.DEATH == vm.getHistoryType(),
                    HistoryType.DEATH
                )
            )
        }

        if (session.isExpired()) {
            vm.setCurrentDate()
        }
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
                .addItemViewType(R.layout.item_history, vm.itemOffset)
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun openTypePicker(view: View) {
        powerMenu = PowerMenu.Builder(context)
            .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
            .addItemList(typeItems)
            .setSelectedMenuColor(ColorUtil.getColor(context!!, R.color.colorPrimary))
            .setSelectedTextColor(Color.WHITE)
            .setOnMenuItemClickListener(this)
            .setLifecycleOwner(this)
            .build()
        powerMenu?.showAsAnchorRightBottom(view)
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

    private fun processSuccess(type: Response.Type, items: List<HistoryItem>) {
        Timber.v("Result Type[%s] Size[%s]", type.name, items.size)
        adapter.setItems(items)
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
}