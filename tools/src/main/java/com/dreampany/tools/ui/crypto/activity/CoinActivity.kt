package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.CoinActivityBinding
import com.dreampany.tools.ui.crypto.adapter.CoinPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Created by roman on 26/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinActivity : InjectActivity() {

    private lateinit var bind: CoinActivityBinding
    private lateinit var adapter: CoinPagerAdapter


    override fun hasBinding(): Boolean = true

    override fun homeUp(): Boolean = true

    override fun layoutRes(): Int = R.layout.coin_activity

    override fun toolbarId(): Int = R.id.toolbar
    
    override fun onStartUi(state: Bundle?) {initUi()
initPager()
    }

    override fun onStopUi() {
     }

    private fun initUi() {
        bind = getBinding()
        //vm = ViewModelProvider(this, factory).get(CoinViewModel::class.java)

        //vm.subscribes(this, Observer { this.processResponse(it) })
    }

    private fun initPager() {
        adapter = CoinPagerAdapter(this)
        bind.pager.adapter = adapter
        TabLayoutMediator(
            bind.tabs,
            bind.pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
               // tab.text = adapter.getTitle(position)
            }).attach()
    }
}