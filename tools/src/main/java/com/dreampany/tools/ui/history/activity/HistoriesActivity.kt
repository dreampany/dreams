package com.dreampany.tools.ui.history.activity

import android.os.Bundle
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.HistoriesActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.history.adapter.HistoryPagerAdapter
import com.dreampany.tools.ui.history.model.HistoryItem
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.content_pager_ad.view.*
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
        ad.loadBanner(this.javaClass.simpleName)
        ad.showInHouseAds(this)
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

    override fun <T> onItem(item: T) {
         if (item is HistoryItem) {
             //bind.icon.setUrl(item.item.favicon)
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