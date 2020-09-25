package com.dreampany.hello.ui.auth

import android.os.Bundle
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.hello.R
import com.dreampany.hello.databinding.AuthActivityBinding
import com.dreampany.hello.databinding.SignupActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

/**
 * Created by roman on 24/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SignupActivity : InjectActivity() {

    private lateinit var bind: SignupActivityBinding

    override val layoutRes: Int = R.layout.signup_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()

        bind.google.setOnSafeClickListener {
            googleSignup()
        }
    }

    private fun googleSignup() {
        /*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()*/
    }
}