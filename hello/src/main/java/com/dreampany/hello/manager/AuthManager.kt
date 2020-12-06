package com.dreampany.hello.manager

import android.app.Activity
import android.content.Intent
import com.dreampany.framework.misc.exts.decodeBase64
import com.dreampany.framework.misc.func.Executors
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.hello.misc.Constants
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
import com.google.android.gms.tasks.Tasks
import com.google.common.collect.Maps
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 12/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AuthManager
@Inject constructor(
    private val ex: Executors
) {

    interface Callback {
        fun onResult(result: FirebaseUser)
        fun onError(error: Throwable)
    }

    enum class Type {
        GOOGLE, FACEBOOK;

        val isGoogle: Boolean get() = this == GOOGLE
        val isFacebook: Boolean get() = this == FACEBOOK
    }

    private val types: MutableMap<Int, Type>
    private val callbacks: MutableMap<Int, Callback>

    private val auth: FirebaseAuth
    private var google: GoogleSignInClient? = null
    private var facebook: CallbackManager? = null

    init {
        types = Maps.newConcurrentMap()
        callbacks = Maps.newConcurrentMap()
        auth = Firebase.auth

    }

    val isSignIn: Boolean
        get() = user != null

    val user: FirebaseUser?
        get() = auth.currentUser

    fun registerCallback(requestCode: Int, callback: Callback) {
        callbacks.put(requestCode, callback)
    }

    fun unregisterCallback(requestCode: Int) {
        callbacks.remove(requestCode)
    }

    @Synchronized
    fun signInAnonymously(): Boolean {
        if (isSignIn) return true
        val task: Task<AuthResult> = auth.signInAnonymously()
        val result = Tasks.await(task)
        return isSignIn
    }

    fun signInGoogle(instance: Activity, requestCode: Int) {
        if (google == null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.Apis.GOOGLE_CLIENT_ID_DREAMPANY_MAIL.decodeBase64)
                .requestEmail()
                .build()
            google = GoogleSignIn.getClient(instance, gso)
        }
        types.put(requestCode, Type.GOOGLE)
        instance.startActivityForResult(google?.signInIntent, requestCode)
    }

    fun signInFacebook(instance: Activity, requestCode: Int) {
        if (facebook == null) {
            facebook = CallbackManager.Factory.create()

            LoginManager.getInstance()
                .registerCallback(facebook, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        val callback: Callback = callbacks.get(requestCode) ?: return
                        loginCredential(result.accessToken, callback)
                    }

                    override fun onCancel() {
                        Timber.e("facebook login cancelled")
                        val callback: Callback = callbacks.get(requestCode) ?: return
                    }

                    override fun onError(error: FacebookException) {
                        val callback: Callback = callbacks.get(requestCode) ?: return

                        Timber.e(error)
                    }

                })
        }
        types.put(requestCode, Type.FACEBOOK)
        LoginManager.getInstance().logInWithReadPermissions(
            instance,
            Arrays.asList("public_profile", "email")
        )
    }

    fun handleResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        val type: Type = types.get(requestCode) ?: return false
        val callback: Callback = callbacks.get(requestCode) ?: return false
        if (type.isGoogle) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task, callback)
            return true
        }
        if (type.isFacebook) {
            return facebook?.onActivityResult(requestCode, resultCode, data) ?: true
        }
        return false
    }

    private fun handleResult(task: Task<GoogleSignInAccount>, callback: Callback) {
        try {
            val account = task.getResult(ApiException::class.java) ?: return
            loginCredential(account.idToken, callback)
        } catch (error: Throwable) {
            Timber.e(error)
        }
    }

    private fun loginCredential(token: String?, callback: Callback) {
        Timber.i("google token: %s", token)
        val credential = GoogleAuthProvider.getCredential(token, null)
        loginCredential(credential, callback)
    }

    private fun loginCredential(token: AccessToken, callback: Callback) {
        Timber.i("facebook token: %s", token.token)
        val credential = FacebookAuthProvider.getCredential(token.token)
        loginCredential(credential, callback)
    }

    // common firebase auth login
    private fun loginCredential(credential: AuthCredential, callback: Callback) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(ex.uiExecutor) { task ->
                if (task.isSuccessful) {
                    val user = user
                    if (user == null) {
                        Timber.e("empty in firebase auth login")
                        val error = SmartError(error = NullPointerException())
                        callback.onError(error)
                    } else {
                        Timber.i("success in firebase auth login: %s", user.toString())
                        callback.onResult(user)
                    }
                } else {
                    Timber.e("error in firebase auth login")
                    val error = SmartError(error = task.exception)
                    callback.onError(error)
                }
            }
    }
}