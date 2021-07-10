package com.dreampany.hi.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dreampany.common.misc.func.SmartError
import com.dreampany.hi.R
import com.dreampany.hi.databinding.RegisterActivityBinding
import com.dreampany.hi.manager.AuthManager
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    companion object {
        const val RC_EMAIL = 501
        const val RC_GOOGLE = 502
        const val RC_FACEBOOK = 503
    }

    @Inject
    internal lateinit var authM: AuthManager

    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<RegisterActivityBinding>(
            this,
            R.layout.register_activity
        )

        binding.google.setOnClickListener {
            loginGoogle()
        }

        authM.registerCallback(RC_GOOGLE, object : AuthManager.Callback {
            override fun onResult(serverAuthCode: String) {


            }

            override fun onResult(result: FirebaseUser) {
                loginGoogle(result)
            }

            override fun onError(error: SmartError) {
                Timber.e(error.error)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = authM.handleResult(requestCode, resultCode, data)
        if (result) return
    }

    private fun loginGoogle(user: FirebaseUser) {

    }

    private fun loginGoogle() {
        authM.signInGoogle(this, RC_GOOGLE)
    }

}
