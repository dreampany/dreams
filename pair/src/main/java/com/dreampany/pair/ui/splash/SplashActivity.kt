package com.dreampany.pair.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.common.misc.extension.open
import com.dreampany.common.ui.activity.BaseInjectorActivity
import com.dreampany.common.ui.vm.factory.ViewModelFactory
import com.dreampany.pair.R
import com.dreampany.pair.ui.auth.activity.AuthActivity
import com.dreampany.pair.ui.auth.activity.LoginActivity
import com.dreampany.pair.ui.auth.activity.RegisterActivity
import com.dreampany.pair.ui.auth.vm.AuthViewModel
import com.dreampany.pair.ui.tutorial.TutorialActivity
import kotlinx.coroutines.Runnable
import javax.inject.Inject

/**
 * Created by roman on 3/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SplashActivity : BaseInjectorActivity() {

    @Inject
    internal lateinit var factory: ViewModelFactory
    private lateinit var vm: AuthViewModel

    override fun layoutId(): Int = R.layout.splash_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
        ex.postToUi(Runnable {
            nextScreen()
        }, 2000L)
    }

    override fun onStopUi() {

    }

    private fun initUi() {
        vm = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
    }

    private fun nextScreen() {
        if (vm.isJoinPressed()) {
            if (vm.isLoggedIn()) {
                // TODO HomeActivity
            } else if (vm.isLoggedOut()) {
                open(LoginActivity::class, true)
            } else {
                open(AuthActivity::class, true)
            }
        } else {
            open(TutorialActivity::class, true)
        }
    }
}