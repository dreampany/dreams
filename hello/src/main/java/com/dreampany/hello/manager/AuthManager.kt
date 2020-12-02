package com.dreampany.hello.manager

import android.app.Activity
import android.content.Intent
import com.dreampany.framework.misc.exts.decodeBase64
import com.dreampany.hello.data.source.firestore.Constants
import com.dreampany.hello.misc.user
import com.dreampany.hello.ui.auth.activity.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 12/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AuthManager @Inject constructor() {

    companion object {
        const val RC_GOOGLE_SIGN_IN = 101
    }

    private val auth: FirebaseAuth
    private var google: GoogleSignInClient? = null

    init {
        auth = Firebase.auth
    }

    val isSignIn: Boolean
        get() = user != null

    val user: FirebaseUser?
        get() = auth.currentUser

    @Synchronized
    fun signInAnonymously(): Boolean {
        if (isSignIn) return true
        val task: Task<AuthResult> = auth.signInAnonymously()
        val result = Tasks.await(task)
        return isSignIn
    }

    fun signInGoogle(instance: Activity) {
        if (google == null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.Api.GOOGLE_CLIENT_ID_DREAMPANY_MAIL.decodeBase64)
                .requestEmail()
                .build()
            google = GoogleSignIn.getClient(instance, gso)
        }
        instance.startActivityForResult(google?.signInIntent, RC_GOOGLE_SIGN_IN)
    }

    fun handleSignInGoogle(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java) ?: return
            loginCredential(account.idToken)
        } catch (error: Throwable) {
            Timber.e(error)
        }
    }

    private fun loginCredential(token: String?) {
        Timber.v("google token: %s", token)
        val credential = GoogleAuthProvider.getCredential(token, null)
        loginCredential(credential)
    }

    private fun loginCredential(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser?.user ?: return@addOnCompleteListener
                    //login(user)
                } else {
                    Timber.e("Failed in login")
                }
            }
    }
}