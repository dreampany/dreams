package com.dreampany.hello.ui.auth

import android.content.Intent
import android.os.Bundle
import com.dreampany.framework.misc.exts.decodeBase64
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.hello.R
import com.dreampany.hello.api.ApiConstants
import com.dreampany.hello.databinding.SignupActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import timber.log.Timber

/**
 * Created by roman on 24/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SignupActivity : InjectActivity() {

    companion object {
        const val RC_GOOGLE_SIGN_IN = 101
    }

    private lateinit var bind: SignupActivityBinding

    override val layoutRes: Int = R.layout.signup_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()

        bind.google.setOnSafeClickListener {
            loginGoogle()
        }
    }

    private fun loginGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ApiConstants.Api.GOOGLE_CLIENT_ID_DREAMPANY_MAIL.decodeBase64)
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(this, gso)
        startActivityForResult(client.signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        try {

        } catch (error: Throwable) {
            Timber.e(error)
        }
    }
}