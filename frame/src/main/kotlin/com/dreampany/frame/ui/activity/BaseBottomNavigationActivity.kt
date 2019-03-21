package com.dreampany.frame.ui.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.Menu
import android.view.MenuItem


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
abstract class BaseBottomNavigationActivity : BaseMenuActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var currentNavigationId: Int = 0

    open fun getNavigationViewId(): Int {
        return 0
    }

    open fun getDefaultSelectedNavigationItemId(): Int {
        return 0
    }

    protected abstract fun onNavigationItem(navigationItemId: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        fireOnStartUi = false
        super.onCreate(savedInstanceState)
        val navigationView = findViewById<BottomNavigationView>(getNavigationViewId())
        navigationView?.setOnNavigationItemSelectedListener(this)
        setSelectedItem(getDefaultSelectedNavigationItemId())
        onStartUi(savedInstanceState)
        getApp().getAnalytics().logEvent(getAnalyticTag(), savedInstanceState)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val targetNavigationId = item.itemId
        if (targetNavigationId != currentNavigationId) {
            onNavigationItem(targetNavigationId)
            currentNavigationId = targetNavigationId
            return true
        }
        return false
    }

    override fun onMenuCreated(menu: Menu) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setSelectedItem(navigationItemId: Int) {
        if (navigationItemId != 0) {
            val navView = findViewById<BottomNavigationView>(getNavigationViewId())
            navView?.post { navView.selectedItemId = navigationItemId }
        }
    }
}