package com.dreampany.hello.ui.auth.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SimpleTextWatcher
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.hello.R
import com.dreampany.hello.data.source.firestore.Constants
import com.dreampany.hello.data.enums.Action
import com.dreampany.hello.data.enums.State
import com.dreampany.hello.data.enums.Subtype
import com.dreampany.hello.data.enums.Type
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.model.User
import com.dreampany.hello.data.source.pref.Pref
import com.dreampany.hello.databinding.LoginActivityBinding
import com.dreampany.hello.manager.AuthManager
import com.dreampany.hello.misc.active
import com.dreampany.hello.misc.inactive
import com.dreampany.hello.misc.user
import com.dreampany.hello.ui.home.activity.HomeActivity
import com.dreampany.hello.ui.vm.AuthViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 24/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoginActivity : InjectActivity() {

    companion object {
        const val RC_GOOGLE_SIGN_IN = 501
        const val RC_FACEBOOK_SIGN_IN = 502
    }

    @Inject
    internal lateinit var pref: Pref

    @Inject
    internal lateinit var auth: AuthManager

    private lateinit var bind: LoginActivityBinding
    private lateinit var vm: AuthViewModel


    private lateinit var user: User

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.login_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
        auth.unregisterCallback(RC_GOOGLE_SIGN_IN)
        auth.unregisterCallback(RC_FACEBOOK_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = auth.handleResult(requestCode, resultCode, data)
        if (result) return
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()
        vm = createVm(AuthViewModel::class)
        vm.subscribe(this, { this.processAuthResponse(it) })

        auth.registerCallback(RC_GOOGLE_SIGN_IN, object : AuthManager.Callback {
            override fun onResult(result: FirebaseUser) {
                loginGoogle(result.user)
            }

            override fun onError(error: Throwable) {
            }
        })

        auth.registerCallback(RC_FACEBOOK_SIGN_IN, object : AuthManager.Callback {
            override fun onResult(result: FirebaseUser) {
                loginFacebook(result.user)
            }

            override fun onError(error: Throwable) {
            }
        })

        bind.inputEmail.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                updateUi()
            }
        })

        bind.inputPassword.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(text: Editable?) {
                updateUi()
            }
        })

        bind.login.setOnSafeClickListener {
            login()
        }

        bind.google.setOnSafeClickListener {
            loginGoogle()
        }

        bind.facebook.setOnSafeClickListener {
            loginFacebook()
        }
    }

    private fun updateUi() {
        if (bind.inputEmail.isEmpty.not() || bind.inputPassword.isEmpty.not()) {
            bind.login.active()
        } else {
            bind.login.inactive()
        }
        bind.layoutEmail.error = null
        bind.layoutPassword.error = null
    }

    private fun login() {
        val email = bind.inputEmail.trimValue
        val password = bind.inputPassword.trimValue
        var valid = true
        if (!email.isEmail) {
            valid = false
            bind.layoutEmail.error = getString(R.string.error_email)
        }
        if (!password.isPassword) {
            valid = false
            bind.layoutPassword.error = getString(R.string.error_password)
        }
        if (valid.not()) return
        vm.read(email, password)
    }

    private fun loginGoogle(user: User) {
        this.user = user
        vm.read(user.id)
    }

    private fun loginFacebook(user: User) {
        this.user = user
        vm.read(user.id)
    }

    private fun loginGoogle() {
        auth.signInGoogle(this, RC_GOOGLE_SIGN_IN)
    }

    private fun loginFacebook() {
        auth.signInFacebook(this, RC_FACEBOOK_SIGN_IN)
    }

    private fun processAuthResponse(response: Response<Type, Subtype, State, Action, Auth>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
            progress(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, Auth>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result, response.state)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResult(result: Auth?, state: State) {
        if (result == null) {
            if (state == State.ID) {
                openAuthInfoUi()
            } else {
                bind.error.show()
            }
            return
        }
        //successful login
        pref.login()
        open(HomeActivity::class, true)
    }

    private fun openAuthInfoUi() {
        val task = UiTask(
            Type.USER,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            user
        )
        open(AuthInfoActivity::class, task)
    }
}