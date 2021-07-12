package com.dreampany.hi.ui.home

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.hi.R
import com.dreampany.hi.databinding.HomeActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeActivityBinding>() {

    override val layoutRes: Int
        get() = R.layout.home_activity

    override fun onStartUi(state: Bundle?) {
         initUi()
    }

    override fun onStopUi() {

    }

    private fun initUi() {
        val navView: BottomNavigationView = binding.navView

        val controller = findNavController(R.id.nav_host)
        val configuration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(controller, configuration)
        navView.setupWithNavController(controller)
    }
}