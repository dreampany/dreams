package com.dreampany.word.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.databinding.FragmentRecyclerBinding
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.util.ViewUtil
import com.dreampany.word.R
import com.dreampany.word.ui.activity.ToolsActivity
import com.dreampany.word.ui.adapter.MoreAdapter
import com.dreampany.word.ui.enums.MoreType
import com.dreampany.word.ui.enums.UiSubtype
import com.dreampany.word.ui.enums.UiType
import com.dreampany.word.ui.model.MoreItem
import com.dreampany.word.ui.model.UiTask
import com.dreampany.word.vm.MoreViewModel
import eu.davidea.flexibleadapter.common.FlexibleItemAnimator
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import java.util.*
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class MoreFragment @Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    private lateinit var bindRecycler: FragmentRecyclerBinding
    private lateinit var vm: MoreViewModel
    private lateinit var adapter: MoreAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_recycler
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()
    }

    override fun onStopUi() {
        vm.clear()
    }

    override fun onResume() {
        super.onResume()
        vm.loads(false)
    }

    override fun onPause() {
        super.onPause()
        vm.removeMultipleSubscription()
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        if (position != RecyclerView.NO_POSITION) {
            val item = adapter.getItem(position)
            showItem(item!!)
            return true
        }
        return false
    }

    private fun initView() {
        setTitle(R.string.more)
        bindRecycler = super.binding as FragmentRecyclerBinding

        vm = ViewModelProviders.of(this, factory).get(MoreViewModel::class.java)
        vm.observeOutputs(this, Observer { this.processResponse(it) })
    }

    private fun initRecycler() {
        bindRecycler.setItems(ObservableArrayList<Any>())
        adapter = MoreAdapter(this)
        adapter.setStickyHeaders(false)
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(Objects.requireNonNull(context)),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_more, 0, 0, 0, 1)
                //.withBottomEdge(false)
                .withEdge(true),
            FlexibleItemAnimator(),
            null, null
        )
    }

    private fun processResponse(response: Response<List<MoreItem>>) {
        if (response is Response.Result<*>) {
            val result = response as Response.Result<List<MoreItem>>
            processSuccess(result.data)
        }
    }

    private fun processSuccess(items: List<MoreItem>) {
        if (adapter.isEmpty) {
            adapter.addItems(items)
        }
    }

    private fun showItem(item: MoreItem) {
        when (item.item.type) {
            MoreType.APPS -> vm.moreApps(getParent())
            MoreType.RATE_US -> vm.rateUs(getParent())
            MoreType.FEEDBACK -> vm.sendFeedback(getParent())
            MoreType.SETTINGS -> {
                var task = UiTask<BaseKt>(false, UiType.MORE, UiSubtype.SETTINGS)
                openActivity(ToolsActivity::class.java, task)
            }
            MoreType.LICENSE -> {
                var task = UiTask<BaseKt>(false, UiType.MORE, UiSubtype.LICENSE)
                openActivity(ToolsActivity::class.java, task)
            }
            MoreType.ABOUT -> {
                var task = UiTask<BaseKt>(false, UiType.MORE, UiSubtype.ABOUT)
                openActivity(ToolsActivity::class.java, task)
            }
            else -> {
                var task = UiTask<BaseKt>(false, UiType.MORE, UiSubtype.ABOUT)
                openActivity(ToolsActivity::class.java, task)
            }
        }
    }
}