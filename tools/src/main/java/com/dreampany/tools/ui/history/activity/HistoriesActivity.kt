package com.dreampany.tools.ui.history.activity

import android.os.Bundle
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.HistoriesActivityBinding
import com.dreampany.tools.misc.extension.setUrl
import com.dreampany.tools.ui.radio.adapter.StationPagerAdapter
import com.dreampany.tools.ui.radio.model.StationItem
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Created by roman on 14/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoriesActivity : InjectActivity() {

    private lateinit var bind: HistoriesActivityBinding
    private lateinit var adapter: StationPagerAdapter

    override fun hasBinding(): Boolean = true

    override fun homeUp(): Boolean = true

    override fun layoutRes(): Int = R.layout.stations_activity

    override fun toolbarId(): Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
        initPager()
    }

    override fun onStopUi() {
    }

    override fun <T> onItem(item: T) {
         if (item is StationItem) {
             bind.icon.setUrl(item.item.favicon)
         }
    }

    private fun initUi() {
        bind = getBinding()
        //vm = ViewModelProvider(this, factory).get(CoinViewModel::class.java)

        //vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initPager() {
        adapter = StationPagerAdapter(this)
        bind.layoutPager.pager.adapter = adapter
        TabLayoutMediator(
            bind.tabs,
            bind.layoutPager.pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = adapter.getTitle(position)
            }).attach()
    }
}