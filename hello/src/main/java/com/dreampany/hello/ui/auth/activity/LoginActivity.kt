package com.dreampany.hello.ui.auth.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.exts.decodeBase64
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.hello.R
import com.dreampany.hello.api.ApiConstants
import com.dreampany.hello.data.enums.Action
import com.dreampany.hello.data.enums.State
import com.dreampany.hello.data.enums.Subtype
import com.dreampany.hello.data.enums.Type
import com.dreampany.hello.data.model.User
import com.dreampany.hello.databinding.LoginActivityBinding
import com.dreampany.hello.misc.user
import com.dreampany.hello.ui.vm.UserViewModel
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
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.*

/**
 * Created by roman on 24/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoginActivity : InjectActivity() {

    companion object {
        const val RC_GOOGLE_SIGN_IN = 101
    }

    private lateinit var bind: LoginActivityBinding
    private lateinit var vm: UserViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient
    private lateinit var manager: CallbackManager

    override val homeUp: Boolean = true
    override val layoutRes: Int = R.layout.login_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
        initAuth()
    }

    override fun onStopUi() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = manager.onActivityResult(requestCode, resultCode, data)
        if (result) return
        if (requestCode == SignupActivity.RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(UserViewModel::class)
        vm.subscribe(this, Observer { this.processResponse(it) })

        bind.google.setOnSafeClickListener {
            loginGoogle()
        }

        bind.facebook.setOnSafeClickListener {
            loginFacebook()
        }
    }

    private fun initAuth() {
        if (::auth.isInitialized) return
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ApiConstants.Api.GOOGLE_CLIENT_ID_DREAMPANY_MAIL.decodeBase64)
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, gso)

        manager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(manager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    loginCredential(result.accessToken)
                }

                override fun onCancel() {
                    Timber.w("facebook login cancelled")
                }

                override fun onError(error: FacebookException) {
                    Timber.e(error)
                }

            })
    }

    private fun loginGoogle() {
        startActivityForResult(client.signInIntent, SignupActivity.RC_GOOGLE_SIGN_IN)
    }

    private fun loginFacebook() {
        LoginManager.getInstance()
            .logInWithReadPermissions(
                this,
                Arrays.asList("public_profile", "email")
            )
    }

    private fun loginCredential(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser?.user ?: return@addOnCompleteListener
                    vm.write(user)
                } else {

                }
            }
    }

    private fun loginCredential(token: String?) {
        Timber.v("google token: %s", token)
        val credential = GoogleAuthProvider.getCredential(token, null)
        loginCredential(credential)
    }

    private fun loginCredential(token: AccessToken) {
        Timber.v("facebook token: %s", token.token)
        val credential = FacebookAuthProvider.getCredential(token.token)
        loginCredential(credential)
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java) ?: return
            loginCredential(account.idToken)
        } catch (error: Throwable) {
            Timber.e(error)
        }
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, User>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, User>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
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

    private fun processResult(result: User?) {
        if (result != null) {

        }
    }
}