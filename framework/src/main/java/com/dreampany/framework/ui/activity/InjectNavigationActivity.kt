package com.dreampany.framework.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.dreampany.framework.R
import com.google.android.material.navigation.NavigationView

/**
 * Created by roman on 12/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class InjectNavigationActivity : InjectActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout

    @IdRes
    private var currentNavigationItemId: Int = 0

    @get:IdRes
    abstract val drawerLayoutId: Int

    @get:IdRes
    abstract val navigationViewId: Int

    @get:IdRes
    abstract val navigationHeaderId: Int

    @get:IdRes
    abstract val selectedNavigationItemId: Int

    protected abstract fun onNavigationItem(navigationItemId: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        startByInject = false
        super.onCreate(savedInstanceState)
        drawer = findViewById<DrawerLayout>(drawerLayoutId)
        val navigationView = findViewById<NavigationView>(navigationViewId)
        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView?.apply {
            setNavigationItemSelectedListener(this@InjectNavigationActivity)
            setCheckedItem(selectedNavigationItemId)
            menu.performIdentifierAction(selectedNavigationItemId, 0)
        }

        //setSelectedItem(selectedNavigationItemId)
        params?.let { app.logEvent(it) }
        onStartUi(savedInstanceState)
    }

    /* override fun onPostCreate(savedInstanceState: Bundle?) {
         super.onPostCreate(savedInstanceState)
         toggle.syncState()
     }

     override fun onConfigurationChanged(newConfig: Configuration) {
         super.onConfigurationChanged(newConfig)
         toggle.onConfigurationChanged(newConfig)
     }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        closeDrawer()
        val targetNavigationItemId = item.itemId
        if (targetNavigationItemId != currentNavigationItemId) {
            currentNavigationItemId = targetNavigationItemId
            ex.postToUi(Runnable {
                onNavigationItem(targetNavigationItemId)
            }, 500)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                closeDrawer()
                return true
            }
        }
        /*if (toggle.onOptionsItemSelected(item)) {
            return true
        }*/
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed()
        }
    }

/*    protected fun setSelectedItem(navigationItemId: Int) {
        if (navigationItemId != 0) {
            val navView = findViewById<NavigationView>(navigationViewId)
            navView?.post {
                navView.setCheckedItem(navigationItemId)
                onNavigationItem(navigationItemId)
                currentNavigationItemId = navigationItemId
            }
        }
    }*/

    protected fun openDrawer(): Boolean {
        if (!isDrawerOpen) {
            drawer.openDrawer(GravityCompat.START)
            return true
        }
        return false
    }

    protected fun closeDrawer(): Boolean {
        if (isDrawerOpen) {
            drawer.closeDrawer(GravityCompat.START)
            return true
        }
        return false
    }

    private val isDrawerOpen: Boolean
        get() = drawer.isDrawerOpen(GravityCompat.START)
}