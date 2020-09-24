package com.dreampany.hello.ui.auth

import android.os.Bundle
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.hello.R
import com.dreampany.hello.databinding.AuthActivityBinding

/**
 * Created by roman on 24/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthActivity : InjectActivity() {

    private lateinit var bind: AuthActivityBinding

    override val layoutRes: Int = R.layout.auth_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind.signup.setOnSafeClickListener {
            open(SignupActivity::class)
        }
    }
}