package com.dreampany.nearby.ui.publish

import android.os.Bundle
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.nearby.manager.AdManager
import javax.inject.Inject
import com.dreampany.nearby.R
import com.dreampany.nearby.databinding.PublishActivityBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.content_pager_ad.view.*

/**
 * Created by roman on 28/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PublishActivity : InjectActivity() {

    @Inject
    internal lateinit var ad: AdManager

    private lateinit var bind: PublishActivityBinding
    private lateinit var adapter: MediaPagerAdapter

    override val homeUp: Boolean = true

    override val layoutRes: Int = R.layout.publish_activity

    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initAd()
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
        adapter = MediaPagerAdapter(this)
        bind.layoutPager.pager.adapter = adapter
        TabLayoutMediator(
            bind.tabs,
            bind.layoutPager.pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = adapter.getTitle(position)
            }).attach()
    }
}