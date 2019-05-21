package com.dreampany.common.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Created by roman on 2019-05-20
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

abstract class BaseNavigationActivity : BaseMenuActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

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