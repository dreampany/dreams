package com.dreampany.lca.ui.activity

import android.os.Bundle
import com.dreampany.framework.misc.extension.open
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.lca.R

/**
 * Created by roman on 3/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SplashActivity : InjectActivity() {

    override val layoutRes: Int = R.layout.splash_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
        ex.postToUi(kotlinx.coroutines.Runnable {
            nextScreen()
        }, 1000L)
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