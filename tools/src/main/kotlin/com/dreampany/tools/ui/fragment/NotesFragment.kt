package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.NoteType
import com.dreampany.tools.data.misc.NoteRequest
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentNotesBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.NoteAdapter
import com.dreampany.tools.ui.model.NoteItem
import com.dreampany.tools.vm.NoteViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by roman on 2019-08-04
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class NotesFragment @Inject constructor() :
    BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentNotesBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var adapter: NoteAdapter
    private lateinit var vm: NoteViewModel
    private lateinit var scroller: OnVerticalScrollListener

    override fun getLayoutId(): Int {
        return R.layout.fragment_notes
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
        session.track()
        request(progress = true)
        initTitleSubtitle()
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onRefresh() {
        super.onRefresh()
        request(progress = true)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {
                openAddNoteUi()
            }
        }
    }

    private fun initTitleSubtitle() {
        setTitle(R.string.title_note)
    }

    private fun initUi() {
        bind = super.binding as FragmentNotesBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty_note, null)
        )

        processUiState(UiState.DEFAULT)

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bind.fab.setOnClickListener(this)

        val adapter = AHBottomNavigationAdapter(getParent(), R.menu.menu_bottom_note_home)
        adapter.setupWithBottomNavigation(bind.bottomNav)
        bind.bottomNav.apply {
            isTranslucentNavigationEnabled = true
            defaultBackgroundColor = ColorUtil.getColor(context, R.color.colorPrimary)
            accentColor = ColorUtil.getColor(context, R.color.colorAccent)
            inactiveColor = R.color.colorPrimaryDark
            isForceTint = true
            titleState = AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE
        }


        vm = ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = NoteAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {}
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollStaggeredLayoutManager(context!!, adapter.getSpanCount()),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_note, adapter.getItemOffset())
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
        val request = NoteRequest(
            type = NoteType.MULTIPLE,
            important = important,
            progress = progress
        )
        vm.load(request)
    }

    private fun openAddNoteUi() {

    }

    private fun processUiState(state: UiState) {
        Timber.v("UiState %s", state.name)
        when (state) {
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
            UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
                initTitleSubtitle()
            }
        }
    }

    fun processMultipleResponse(response: Response<List<NoteItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            Timber.v("processMultipleResponse %s", result.loading)
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<NoteItem>>
            processSuccess(result.type, result.data)
        }
    }

    private fun processSuccess(type: Response.Type, items: List<NoteItem>) {
        Timber.v("Result Type[%s] Size[%s]", type.name, items.size)
        adapter.addItems(items)
        ex.postToUi({ processUiState(UiState.EXTRA) }, 500L)
    }
}