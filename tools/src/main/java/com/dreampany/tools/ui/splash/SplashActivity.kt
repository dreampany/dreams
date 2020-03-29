package com.dreampany.tools.ui.splash

import android.os.Bundle
import com.dreampany.common.misc.extension.open
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.ui.home.activity.HomeActivity
import kotlinx.coroutines.Runnable

/**
 * Created by roman on 3/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SplashActivity : InjectActivity() {

    //@Inject
    //internal lateinit var factory: ViewModelFactory
    //private lateinit var vm: AuthViewModel

    override fun layoutRes(): Int = R.layout.splash_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
        ex.postToUi(Runnable {
            nextScreen()
        }, 0L)
    }

    override fun onStopUi() {

    }

    private fun initUi() {
        //vm = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
    }

    private fun nextScreen() {
        open(HomeActivity::class, true)
/*        if (vm.isJoinPressed()) {
            if (vm.isLoggedIn()) {
                open(HomeActivity::class, true)
            } else if (vm.isLoggedOut()) {
                open(LoginActivity::class, true)
            } else {
                open(AuthActivity::class, true)
            }
        } else {
            open(TutorialActivity::class, true)
        }*/
    }
}