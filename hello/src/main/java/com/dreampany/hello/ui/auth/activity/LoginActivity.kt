package com.dreampany.hello.ui.auth.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SimpleTextWatcher
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.hello.R
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
import com.dreampany.hello.misc.auth
import com.dreampany.hello.misc.inactive
import com.dreampany.hello.misc.user
import com.dreampany.hello.ui.home.activity.HomeActivity
import com.dreampany.hello.ui.vm.AuthViewModel
import com.google.firebase.auth.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 24/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoginActivity : InjectActivity() {

    companion object {
        const val RC_EMAIL = 501
        const val RC_GOOGLE = 502
        const val RC_FACEBOOK = 503
    }

    @Inject
    internal lateinit var pref: Pref

    @Inject
    internal lateinit var authM: AuthManager

    private lateinit var bind: LoginActivityBinding
    private lateinit var vm: AuthViewModel

    private lateinit var type: Auth.Type
    private lateinit var input: FirebaseUser
    private lateinit var auth: Auth
    private lateinit var user: User

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.login_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
        updateUi()
    }

    override fun onStopUi() {
        authM.unregisterCallback(RC_EMAIL)
        authM.unregisterCallback(RC_GOOGLE)
        authM.unregisterCallback(RC_FACEBOOK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = authM.handleResult(requestCode, resultCode, data)
        if (result) return
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()
        vm = createVm(AuthViewModel::class)
        vm.subscribe(this, { this.processAuthResponse(it) })

        bind.inputEmail.setOnLongClickListener(View.OnLongClickListener {
            it.requestFocus()
            false
        })

        bind.inputPassword.setOnLongClickListener(View.OnLongClickListener {
            it.requestFocus()
            false
        })

        authM.registerCallback(RC_EMAIL, object : AuthManager.Callback {
            override fun onResult(result: FirebaseUser) {
                loginEmail(result)
            }

            override fun onError(error: Throwable) {
            }
        })

        authM.registerCallback(RC_GOOGLE, object : AuthManager.Callback {
            override fun onResult(result: FirebaseUser) {
                loginGoogle(result)
            }

            override fun onError(error: Throwable) {
            }
        })

        authM.registerCallback(RC_FACEBOOK, object : AuthManager.Callback {
            override fun onResult(result: FirebaseUser) {
                loginFacebook(result)
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
            loginEmail()
        }

        bind.google.setOnSafeClickListener {
            loginGoogle()
        }

        bind.facebook.setOnSafeClickListener {
            loginFacebook()
        }
    }

    private fun updateUi() {
        if (bind.inputEmail.trimValue.isEmail.not() || bind.inputEmail.isEmpty || bind.inputPassword.isEmpty) {
            bind.login.inactive()
        } else {
            bind.login.active()
        }
        bind.layoutEmail.error = null
        bind.layoutPassword.error = null
    }

    private fun loginEmail() {
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
        // Get Firebase User
        authM.signInEmail(email, password, RC_EMAIL)
    }

    private fun loginEmail(user: FirebaseUser) {
        this.type = Auth.Type.EMAIL
        this.input = user
        vm.read(user.uid)
    }

    private fun loginGoogle(user: FirebaseUser) {
        this.type = Auth.Type.GOOGLE
        this.input = user
        vm.read(user.uid)
    }

    private fun loginFacebook(user: FirebaseUser) {
        this.type = Auth.Type.FACEBOOK
        this.input = user
        vm.read(user.uid)
    }

    private fun loginGoogle() {
        authM.signInGoogle(this, RC_GOOGLE)
    }

    private fun loginFacebook() {
        authM.signInFacebook(this, RC_FACEBOOK)
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
        if (result != null) {
            auth = result
            auth.type = type
            pref.write(auth)
            if (auth.registered) {
                openHomeUi()
            } else {
                openAuthInfoUi()
            }
            return
        }
        if (::input.isInitialized) {
            auth = input.auth(ref)
            user = input.user(ref)
            auth.type = type
            pref.write(auth)
            pref.write(user)
            openAuthInfoUi()
        }
    }

    private fun openAuthInfoUi() {
        pref.signIn()
        val task = UiTask(
            Type.AUTH,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            auth
        )
        open(AuthInfoActivity::class, task)
    }

    private fun openHomeUi() {
        open(HomeActivity::class, true)
    }
}