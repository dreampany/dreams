package com.dreampany.crypto.ui.home.activity

import android.os.Bundle
import com.dreampany.crypto.data.model.Coin
import com.dreampany.crypto.databinding.CoinActivityBinding
import com.dreampany.crypto.ui.home.adapter.CoinPagerAdapter
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.google.android.material.tabs.TabLayoutMediator
import com.dreampany.crypto.R
import com.dreampany.crypto.api.misc.ApiConstants
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.manager.AdManager
import com.dreampany.crypto.misc.exts.setUrl
import kotlinx.android.synthetic.main.content_pager_ad.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 26/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinActivity : InjectActivity() {

    @Inject
    internal lateinit var ad: AdManager

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
        initAd()
        //setTitle(input.name)
        loadUi()
        ad.loadBanner(this.javaClass.simpleName)
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        ad.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ad.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    private fun initUi() {
        bind = getBinding()
        bind.icon.setUrl(
            String.format(
                Locale.ENGLISH,
                ApiConstants.CoinMarketCap.IMAGE_URL,
                input.id
            )
        )
        bind.textName.text = input.name
        bind.textSymbol.text = input.symbol
    }

    private fun initPager() {
        if (!::adapter.isInitialized) {
            adapter = CoinPagerAdapter(this)
            bind.layoutPager.pager.adapter = adapter
            TabLayoutMediator(
                bind.tabs,
                bind.layoutPager.pager,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    tab.text = adapter.getTitle(position)
                }).attach()
        }
    }

    private fun initAd() {
        ad.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun loadUi() {
        adapter.addItems(input)
    }
}