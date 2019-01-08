package com.dreampany.frame.ui.activity

import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import com.dreampany.frame.R


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
abstract class BaseNavigationActivity: BaseMenuActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentNavigationId: Int = 0
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    open fun getDrawerLayoutId(): Int {
        return 0
    }

    open fun getNavigationViewId(): Int {
        return 0
    }

    open fun getNavigationHeaderId(): Int {
        return 0
    }

    open fun getOpenDrawerDescRes(): Int {
        return R.string.navigation_drawer_open
    }

    open fun getCloseDrawerDescRes(): Int {
        return R.string.navigation_drawer_close
    }

    open fun getDefaultSelectedNavigationItemId(): Int {
        return 0
    }

    open fun getNavigationTitle(navigationItemId: Int): String? {
        return null
    }

    protected abstract fun onNavigationItem(navItemId: Int)

    protected abstract fun onDrawerOpening()

    protected abstract fun onDrawerClosing()
}