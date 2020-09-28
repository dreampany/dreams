package com.dreampany.hello.ui.splash

import android.os.Bundle
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.hello.R
import com.dreampany.hello.data.source.pref.Pref
import com.dreampany.hello.ui.auth.activity.AuthActivity
import com.dreampany.hello.ui.auth.activity.AuthInfoActivity
import com.dreampany.hello.ui.home.activity.HomeActivity
import kotlinx.coroutines.Runnable
import javax.inject.Inject

/**
 * Created by roman on 3/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SplashActivity : InjectActivity() {

    @Inject
    internal lateinit var pref: Pref

    override val layoutRes: Int = R.layout.splash_activity

    override fun onStartUi(state: Bundle?) {
        ex.postToUi(Runnable { nextUi() })
    }

    override fun onStopUi() {

    }

    private fun nextUi() {
        if (pref.isStarted || pref.isLoggedIn) {
            open(HomeActivity::class, true)
        } else if (pref.isSignUpIn) {
            open(AuthInfoActivity::class, true)
        } else {
            open(AuthActivity::class, true)
        }
    }
}