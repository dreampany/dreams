package com.dreampany.crypto.ui.home.activity

import android.os.Bundle
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.databinding.CoinActivityBinding
import com.dreampany.crypto.ui.home.adapter.CoinPagerAdapter
import com.dreampany.framework.misc.extension.task
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.google.android.material.tabs.TabLayoutMediator
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type

/**
 * Created by roman on 26/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinActivity : InjectActivity() {

    private lateinit var bind: CoinActivityBinding
    private lateinit var adapter: CoinPagerAdapter

    private lateinit var input: Coin

    override val homeUp: Boolean = true

    override val layoutRes: Int = R.layout.coin_activity

    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Coin>
        input = task.input ?: return
        initUi()
        initPager()
        setTitle(input.name)
        loadUi()
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
                tab.text = adapter.getTitle(position)
            }).attach()
    }

    private fun loadUi() {
        adapter.addItems(input)
    }
}