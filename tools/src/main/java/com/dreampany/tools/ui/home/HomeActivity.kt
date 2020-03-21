package com.dreampany.tools.ui.home

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.HomeActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HomeActivity : InjectActivity() {

    private lateinit var bind: HomeActivityBinding

    override fun hasBinding(): Boolean = true

    override fun layoutId(): Int = R.layout.home_activity

    override fun toolbarId(): Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi () {
        bind = getBinding()
        val config = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, config)
        bind.navView.setupWithNavController(navController)
    }
}