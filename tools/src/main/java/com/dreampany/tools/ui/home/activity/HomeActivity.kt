package com.dreampany.tools.ui.home.activity

import android.os.Bundle
import com.dreampany.common.ui.activity.InjectBottomNavigationActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.HomeActivityBinding
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
    internal lateinit var home: Lazy<HomeFragment>

    private lateinit var bind: HomeActivityBinding

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.home_activity

    override fun toolbarId(): Int = R.id.toolbar

    override fun getNavigationViewId(): Int = R.id.navigation_view

    override fun selectedNavigationItemId(): Int = R.id.navigation_home

    override fun onNavigationItem(navigationItemId: Int) {
        when (navigationItemId) {
            R.id.navigation_home -> {
                commitFragment(HomeFragment::class, home, R.id.layout)
            }
            R.id.navigation_dashboard -> {

            }
            R.id.navigation_notifications -> {

            }
        }
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = getBinding()

    }
}