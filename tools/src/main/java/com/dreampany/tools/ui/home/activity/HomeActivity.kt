package com.dreampany.tools.ui.home.activity

import android.os.Bundle
import com.dreampany.framework.ui.activity.InjectBottomNavigationActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.HomeActivityBinding
import com.dreampany.tools.manager.AdManager
import com.dreampany.tools.ui.more.MoreFragment
import com.dreampany.tools.ui.home.fragment.HomeFragment
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HomeActivity : InjectBottomNavigationActivity() {

    @Inject
    internal lateinit var ad: AdManager

    @Inject
    internal lateinit var home: Lazy<HomeFragment>

    @Inject
    internal lateinit var more: Lazy<MoreFragment>

    private lateinit var bind: HomeActivityBinding

    override fun doubleBackPressed(): Boolean = true

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.home_activity

    override fun toolbarId(): Int = R.id.toolbar

    override fun getNavigationViewId(): Int = R.id.navigation_view

    override fun selectedNavigationItemId(): Int = R.id.navigation_home

    override fun onNavigationItem(navigationItemId: Int) {
        when (navigationItemId) {
            R.id.navigation_home -> {
                setTitle(R.string.home)
                commitFragment(HomeFragment::class, home, R.id.layout)
            }
            R.id.navigation_more -> {
                setTitle(R.string.more)
                commitFragment(MoreFragment::class, more, R.id.layout)
            }
        }
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initAd()
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
}