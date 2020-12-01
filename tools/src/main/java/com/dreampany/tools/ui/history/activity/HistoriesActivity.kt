package com.dreampany.tools.ui.history.activity

import android.os.Bundle
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.HistoriesActivityBinding
import com.dreampany.tools.manager.AdsManager
import com.dreampany.tools.ui.history.adapter.HistoryPagerAdapter
import com.dreampany.tools.ui.history.model.HistoryItem
import com.google.android.material.tabs.TabLayoutMediator
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by roman on 14/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoriesActivity : InjectActivity() {

    @Inject
    internal lateinit var ads: AdsManager

    private lateinit var bind: HistoriesActivityBinding
    private lateinit var adapter: HistoryPagerAdapter

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.histories_activity
    override val toolbarId: Int = R.id.toolbar

    override val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, Constant.Param.screen(this))

            params.put(Constant.Event.key(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initAd()
        initUi()
        initPager()
        ads.loadBanner(this.javaClass.simpleName)
        ads.showInHouseAds(this)
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        ads.resumeBanner(this.javaClass.simpleName)
    }

    override fun onPause() {
        ads.pauseBanner(this.javaClass.simpleName)
        super.onPause()
    }

    override fun <T> onInput(item: T) {
         if (item is HistoryItem) {
             //bind.icon.setUrl(item.item.favicon)
         }
    }

    private fun initAd() {
        ads.initAd(
            this,
            this.javaClass.simpleName,
            findViewById(R.id.adview),
            R.string.interstitial_ad_unit_id,
            R.string.rewarded_ad_unit_id
        )
    }

    private fun initUi() {
        bind = binding()
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