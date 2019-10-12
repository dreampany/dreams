package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentRadioHomeBinding
import com.dreampany.tools.ui.adapter.StationAdapter
import com.dreampany.tools.ui.adapter.WordAdapter
import com.dreampany.tools.ui.model.StationItem
import com.dreampany.tools.ui.model.WordItem
import com.dreampany.tools.ui.vm.WordViewModel
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import javax.inject.Inject

/**
 * Created by roman on 2019-10-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class RadioHomeFragment
@Inject constructor() : BaseMenuFragment(), SmartAdapter.OnUiItemClickListener<StationItem?, Action?> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager

    private lateinit var bind: FragmentRadioHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var vm: WordViewModel
    private lateinit var adapter: StationAdapter
    private lateinit var scroller: OnVerticalScrollListener

    override fun getLayoutId(): Int {
        return R.layout.fragment_radio_home
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_radio
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()
    }

    override fun onStopUi() {
    }

    override fun onClick(view: View, item: StationItem?, action: Action?) {
    }

    override fun onLongClick(view: View, item: StationItem?, action: Action?) {
    }

    private fun initUi() {
        bind = super.binding as FragmentRadioHomeBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = StationAdapter(listener = this)
        adapter.setStickyHeaders(false)
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_station, adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
    }
}