package com.dreampany.hi.manager

import android.app.Activity
import android.content.Intent
import com.dreampany.hi.misc.Constant
import com.dreampany.hi.misc.Executors
import com.dreampany.hi.misc.SmartError
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
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class AuthManager
@Inject constructor(
    private val ex: Executors
) {

    interface Callback {
        fun onResult(serverAuthCode: String)
        fun onResult(result: FirebaseUser)
        fun onError(error: SmartError)
    }

    enum class Type {
        EMAIL, GOOGLE, FACEBOOK;

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

    val user: FirebaseUser? get() = auth.currentUser

    val logged: Boolean get() = user != null

    fun registerCallback(requestCode: Int, callback: Callback) {
        callbacks.put(requestCode, callback)
    }

    fun unregisterCallback(requestCode: Int) {
        callbacks.remove(requestCode)
    }

    @Synchronized
    fun signInAnonymously(): Boolean {
        if (logged) return true
        val task: Task<AuthResult> = auth.signInAnonymously()
        val result = Tasks.await(task)
        return logged
    }

/*    fun registerEmail(email: String, password: String, requestCode: Int) {
        val task: Task<AuthResult> = auth.createUserWithEmailAndPassword(email, password)
        val result = Tasks.await(task)
        val callback: Callback = callbacks.get(requestCode) ?: return
        val user = result.user ?: return
        callback.onResult(user)
    }*/

    @Synchronized
    fun signUpEmail(email: String, password: String, requestCode: Int) {
        val task: Task<AuthResult> = auth.createUserWithEmailAndPassword(email, password)
        task.addOnSuccessListener { result ->
            val callback: Callback = callbacks.get(requestCode) ?: return@addOnSuccessListener
            val user = result.user ?: return@addOnSuccessListener
            callback.onResult(user)
        }.addOnFailureListener { error ->
            Timber.e(error)
            val callback: Callback = callbacks.get(requestCode) ?: return@addOnFailureListener
            callback.onError(SmartError(error = error))
        }
    }

    @Synchronized
    fun signInEmail(email: String, password: String, requestCode: Int) {
        val task: Task<AuthResult> = auth.signInWithEmailAndPassword(email, password)
        task.addOnSuccessListener { result ->
            val callback: Callback = callbacks.get(requestCode) ?: return@addOnSuccessListener
            val user = result.user ?: return@addOnSuccessListener
            callback.onResult(user)
        }.addOnFailureListener { error ->
            Timber.e(error)
            val callback: Callback = callbacks.get(requestCode) ?: return@addOnFailureListener
            callback.onError(SmartError(error = error))
        }
    }


    @Synchronized
    fun signInGoogle(instance: Activity, requestCode: Int) {
        if (google == null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constant.Apis.GOOGLE_CLIENT_ID_DREAMPANY_MAIL)
                .requestServerAuthCode(Constant.Apis.GOOGLE_CLIENT_ID_DREAMPANY_MAIL)
                .requestEmail()
                .build()
            google = GoogleSignIn.getClient(instance, gso)
        }
        types.put(requestCode, Type.GOOGLE)
        instance.startActivityForResult(google?.signInIntent, requestCode)
    }

    @Synchronized
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

    @Synchronized
    fun handleResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        forServerAuthCode: Boolean = false
    ): Boolean {
        val type: Type = types.get(requestCode) ?: return false
        val callback: Callback = callbacks.get(requestCode) ?: return false
        if (type.isGoogle) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task, callback, forServerAuthCode)
            return true
        }
        if (type.isFacebook) {
            return facebook?.onActivityResult(requestCode, resultCode, data) ?: true
        }
        return false
    }

    private fun handleResult(
        task: Task<GoogleSignInAccount>,
        callback: Callback,
        forServerAuthCode: Boolean
    ) {
        try {
            val account = task.getResult(ApiException::class.java) ?: return
            if (forServerAuthCode) {
                val code = account.serverAuthCode
                callback.onResult(code)
            } else {
                loginCredential(account.idToken, callback)
            }
        } catch (error: Throwable) {
            Timber.e(error)
            val error = SmartError(error = error)
            callback.onError(error)
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