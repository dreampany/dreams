package com.dreampany.hi.ui.splash

import android.os.Bundle
import com.dreampany.common.misc.exts.open
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.hi.R
import com.dreampany.hi.databinding.SplashActivityBinding
import com.dreampany.hi.misc.Prefs
import com.dreampany.hi.ui.auth.AuthActivity
import com.dreampany.hi.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashActivityBinding>() {

    @Inject
    internal lateinit var pref: Prefs

    override val layoutRes: Int
        get() = R.layout.splash_activity

    override fun onStartUi(state: Bundle?) {
        ex.postToUi { nextUi() }
    }

    override fun onStopUi() {
    }

    private fun nextUi() {
        if (pref.isStarted || pref.isLogged) {
            openHomeUi()
        } else if (pref.isSigned) {
            openAuthInfoUi()
        } else {
            open(AuthActivity::class, true)
        }
    }

    private fun openHomeUi() {
        open(HomeActivity::class, true)
    }

    private fun openAuthInfoUi() {
        /* val auth = pref.auth ?: return
         val task = UiTask(
             Type.AUTH,
             Subtype.DEFAULT,
             State.DEFAULT,
             Action.DEFAULT,
             auth
         )
         open(AuthInfoActivity::class, task, true)*/
    }


}