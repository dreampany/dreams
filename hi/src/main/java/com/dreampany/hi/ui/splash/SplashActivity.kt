package com.dreampany.hi.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dreampany.hi.R
import com.dreampany.hi.databinding.SplashActivityBinding
import com.dreampany.hi.misc.Executors
import com.dreampany.hi.misc.Prefs
import com.dreampany.hi.open
import com.dreampany.hi.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Runnable
/**
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    internal lateinit var ex: Executors

    @Inject
    internal lateinit var pref: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<SplashActivityBinding>(
            this,
            R.layout.splash_activity
        )

        ex.postToUi { nextUi() }
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
        //open(HomeActivity::class, true)
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