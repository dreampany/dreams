package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import com.dreampany.framework.misc.extension.task
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.crypto.CryptoAction
import com.dreampany.tools.data.enums.crypto.CryptoState
import com.dreampany.tools.data.enums.crypto.CryptoSubtype
import com.dreampany.tools.data.enums.crypto.CryptoType
import com.dreampany.tools.data.model.crypto.Coin
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

    private lateinit var input : Coin

    override fun hasBinding(): Boolean = true

    override fun homeUp(): Boolean = true

    override fun layoutRes(): Int = R.layout.coin_activity

    override fun toolbarId(): Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
        initPager()
        val task : UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Coin> = (task ?: return) as UiTask<CryptoType, CryptoSubtype, CryptoState, CryptoAction, Coin>
        input = task.input ?: return
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