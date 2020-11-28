package com.dreampany.radio.ui.home.activity

import android.os.Bundle
import com.dreampany.radio.R
import com.dreampany.radio.databinding.HomeActivityBinding
import com.dreampany.radio.manager.AdsManager
import com.dreampany.radio.ui.home.fragment.HomeFragment
import com.dreampany.radio.ui.settings.SettingsFragment
import com.dreampany.framework.ui.activity.InjectBottomNavigationActivity
import com.dreampany.radio.ui.fragment.SearchFragment
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
    internal lateinit var ads: AdsManager

    @Inject
    internal lateinit var home: Lazy<HomeFragment>

    @Inject
    internal lateinit var search: Lazy<SearchFragment>

    @Inject
    internal lateinit var settings: Lazy<SettingsFragment>

    private lateinit var bind: HomeActivityBinding

    override val doubleBackPressed: Boolean = true
    override val layoutRes: Int = R.layout.home_activity
    override val toolbarId: Int = R.id.toolbar
    override val navigationViewId: Int get() = R.id.navigation_view
    override val selectedNavigationItemId: Int get() = R.id.navigation_home

    override fun onStartUi(state: Bundle?) {
        initAd()
        initUi()
        ads.loadBanner(this.javaClass.simpleName)
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

    override fun onNavigationItem(navigationItemId: Int) {
        when (navigationItemId) {
            R.id.navigation_home -> {
                setTitle(R.string.home)
                commitFragment(HomeFragment::class, home, R.id.layout)
            }
            R.id.navigation_search -> {
                setTitle(R.string.search)
                commitFragment(SearchFragment::class, search, R.id.layout)
            }
            R.id.navigation_settings -> {
                setTitle(R.string.settings)
                commitFragment(SettingsFragment::class, settings, R.id.layout)
            }
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
        if (::bind.isInitialized) return
        bind = binding()
    }
}