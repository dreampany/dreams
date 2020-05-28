package com.dreampany.tools.ui.history.activity

import android.os.Bundle
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.HistoriesActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.history.adapter.HistoryPagerAdapter
import com.dreampany.tools.ui.history.model.HistoryItem
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

/**
 * Created by roman on 14/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoriesActivity : InjectActivity() {

    @Inject
    internal lateinit var ad: AdManager

    private lateinit var bind: HistoriesActivityBinding
    private lateinit var adapter: HistoryPagerAdapter

    override val homeUp: Boolean = true

    override val layoutRes: Int = R.layout.histories_activity

    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
        initPager()
    }

    override fun onStopUi() {
    }

    override fun <T> onItem(item: T) {
         if (item is HistoryItem) {
             //bind.icon.setUrl(item.item.favicon)
         }
    }

    private fun initUi() {
        bind = getBinding()
    }

    private fun initPager() {
        adapter = HistoryPagerAdapter(this)
        bind.layoutPager.pager.adapter = adapter
        TabLayoutMediator(
            bind.tabs,
            bind.layoutPager.pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = adapter.getTitle(position)
            }).attach()
    }
}