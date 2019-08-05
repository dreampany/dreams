package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.graphics.ColorUtils
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModelProvider
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentNoteHomeBinding
import com.dreampany.tools.ui.adapter.NoteAdapter
import com.dreampany.tools.ui.enums.UiState
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager
import javax.inject.Inject


/**
 * Created by roman on 2019-08-04
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class NoteHomeFragment @Inject constructor() :
    BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentNoteHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var adapter: NoteAdapter
    private lateinit var scroller: OnVerticalScrollListener

    override fun getLayoutId(): Int {
        return R.layout.fragment_note_home
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()
        session.track()
        //  request(progress = true)
        initTitleSubtitle()
    }

    override fun onStopUi() {
        //processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onRefresh() {
        super.onRefresh()
        //request(progress = true)
    }

    private fun initTitleSubtitle() {
        setTitle(R.string.title_note)
    }

    private fun initView() {
        bind = super.binding as FragmentNoteHomeBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.NONE.name,
            LayoutInflater.from(context).inflate(R.layout.item_none, null)
        )

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


        // vm = ViewModelProviders.of(this, factory).get(ApkViewModel::class.java)
        // vm.observeUiState(this, Observer { this.processUiState(it) })
        // vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
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
}