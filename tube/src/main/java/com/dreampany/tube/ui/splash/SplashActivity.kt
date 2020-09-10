package com.dreampany.tube.ui.splash

import android.os.Bundle
import com.dreampany.framework.misc.exts.decodeBase64
import com.dreampany.framework.misc.exts.encodeBase64
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tube.R
import com.dreampany.tube.api.misc.ApiConstants
import com.dreampany.tube.data.source.pref.AppPref
import com.dreampany.tube.ui.home.activity.HomeActivity
import com.dreampany.tube.ui.settings.activity.CategoriesActivity
import kotlinx.coroutines.Runnable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 3/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SplashActivity : InjectActivity() {

    @Inject
    internal lateinit var pref : AppPref

    override val layoutRes: Int = R.layout.splash_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
        ex.postToUi(Runnable {
            nextScreen()
        }, 1000L)
    }

    override fun onStopUi() {

    }

    private fun initUi() {
        //vm = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        val data = ApiConstants.Youtube.API_KEY_DREAMPANY_MAIL.encodeBase64
        val real = data.decodeBase64

        Timber.v("Main %s", ApiConstants.Youtube.API_KEY_DREAMPANY_MAIL)
        Timber.v("Encoded %s", data)
        Timber.v("Decoded %s", real)
    }

    private fun nextScreen() {
        if (pref.isCategoriesSelected) {
            open(HomeActivity::class, true)
        } else {
            open(CategoriesActivity::class, true)
        }
        //open(HomeActivity::class, true)
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