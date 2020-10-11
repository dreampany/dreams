package com.dreampany.tools.ui.radio.activity

import android.os.Bundle
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.StationsActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.misc.exts.setUrl
import com.dreampany.tools.ui.radio.adapter.StationPagerAdapter
import com.dreampany.tools.ui.radio.model.StationItem
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.content_pager_ad.view.*
import javax.inject.Inject

/**
 * Created by roman on 14/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationsActivity : InjectActivity() {

    @Inject
    internal lateinit var ad: AdManager

    private lateinit var bind: StationsActivityBinding
    private lateinit var adapter: StationPagerAdapter

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.stations_activity
    override val toolbarId: Int = R.id.toolbar

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, "StationsActivity")

            params.put(Constant.Event.activity(this), param)
            return params
        }

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

    override fun <T> onItem(item: T) {
         if (item is StationItem) {
             bind.icon.setUrl(item.input.favicon)
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