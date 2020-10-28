package com.dreampany.tube.ui.splash

import android.os.Bundle
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tube.R
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.databinding.SplashActivityBinding
import com.dreampany.tube.ui.home.activity.HomeActivity
import com.dreampany.tube.ui.settings.activity.PagesActivity
import com.dreampany.tube.ui.vm.PageViewModel
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
    internal lateinit var pref: Prefs

    private lateinit var bind : SplashActivityBinding
    private lateinit var vm: PageViewModel

    override val layoutRes: Int = R.layout.splash_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
        ex.postToUi(Runnable { openNextUi() })
    }

    override fun onStopUi() {

    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(PageViewModel::class)
    }

    private fun openNextUi() {
        if (pref.isPagesSelected) {
            open(HomeActivity::class, true)
        } else if (pref.isCategoriesSelected) {
            vm.backupPages()
            open(HomeActivity::class, true)
        } else {
            open(PagesActivity::class, true)
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