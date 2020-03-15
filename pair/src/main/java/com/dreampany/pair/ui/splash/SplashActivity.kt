package com.dreampany.pair.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.dreampany.common.ui.activity.BaseInjectorActivity
import com.dreampany.pair.R
import com.dreampany.pair.ui.tutorial.TutorialActivity
import kotlinx.coroutines.Runnable

/**
 * Created by roman on 3/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SplashActivity : BaseInjectorActivity() {

    override fun layoutId(): Int = R.layout.splash_activity

    override fun onStartUi(state: Bundle?) {

        Handler().postDelayed(Runnable {
            startActivity(Intent(this@SplashActivity, TutorialActivity::class.java))
            this@SplashActivity.finish()
        }, 2000)
    }

    override fun onStopUi() {

    }
}