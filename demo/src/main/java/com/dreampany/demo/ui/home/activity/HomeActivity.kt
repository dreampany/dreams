package com.dreampany.demo.ui.home.activity

import android.os.Bundle
import com.dreampany.demo.R
import com.dreampany.demo.databinding.HomeActivityBinding
import com.dreampany.demo.manager.AdManager
import com.dreampany.demo.ui.home.fragment.HomeFragment
import com.dreampany.demo.ui.settings.SettingsFragment
import com.dreampany.framework.ui.activity.InjectBottomNavigationActivity
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
    internal lateinit var settings: Lazy<SettingsFragment>

    private lateinit var bind: HomeActivityBinding

    override val doubleBackPressed: Boolean = true
    override val layoutRes: Int = R.layout.home_activity
    override val toolbarId: Int = R.id.toolbar
    override val navigationViewId: Int get() = R.id.navigation_view
    override val selectedNavigationItemId: Int get() = R.id.navigation_home

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

    override fun onNavigationItem(navigationItemId: Int) {
        when (navigationItemId) {
            R.id.navigation_home -> {
                setTitle(R.string.home)
                commitFragment(HomeFragment::class, home, R.id.layout)
            }
            R.id.navigation_settings -> {
                setTitle(R.string.settings)
                commitFragment(SettingsFragment::class, settings, R.id.layout)
            }
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
}