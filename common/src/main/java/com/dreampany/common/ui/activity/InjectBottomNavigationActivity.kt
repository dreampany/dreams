package com.dreampany.common.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class InjectBottomNavigationActivity : InjectActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    @IdRes
    private var currentNavigationItemId: Int = 0

    @IdRes
    open fun getNavigationViewId(): Int = 0

    @IdRes
    open fun selectedNavigationItemId(): Int = 0

    protected abstract fun onNavigationItem(navigationItemId: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        fireOnStartUi = false
        super.onCreate(savedInstanceState)
        val navigationView = findViewById<BottomNavigationView>(getNavigationViewId())
        navigationView?.setOnNavigationItemSelectedListener(this)
        setSelectedItem(selectedNavigationItemId())
        onStartUi(savedInstanceState)
        //getApp().throwAnalytics(Constants.Event.ACTIVITY, getScreen())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val targetNavigationItemId = item.itemId
        if (targetNavigationItemId != currentNavigationItemId) {
            onNavigationItem(targetNavigationItemId)
            currentNavigationItemId = targetNavigationItemId
            return true
        }
        return false
    }

    fun setSelectedItem(navigationItemId: Int) {
        if (navigationItemId != 0) {
            val navView = findViewById<BottomNavigationView>(getNavigationViewId())
            navView?.post { navView.selectedItemId = navigationItemId }
        }
    }
}