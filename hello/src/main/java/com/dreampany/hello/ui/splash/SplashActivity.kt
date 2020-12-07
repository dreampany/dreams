package com.dreampany.hello.ui.splash

import android.os.Bundle
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.hello.R
import com.dreampany.hello.data.enums.Action
import com.dreampany.hello.data.enums.State
import com.dreampany.hello.data.enums.Subtype
import com.dreampany.hello.data.enums.Type
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
        if (pref.isStarted || pref.isLogged) {
            openHomeUi()
        } else if (pref.isSignIn) {
            openAuthInfoUi()
        } else {
            open(AuthActivity::class, true)
        }
    }

    private fun openHomeUi() {
        open(HomeActivity::class, true)
    }

    private fun openAuthInfoUi() {
        val auth = pref.auth ?: return
        val task = UiTask(
            Type.AUTH,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            auth
        )
        open(AuthInfoActivity::class, task, true)
    }
}