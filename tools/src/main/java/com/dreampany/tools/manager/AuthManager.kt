package com.dreampany.tools.manager

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 26/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AuthManager
@Inject constructor(

) {
    private val auth: FirebaseAuth

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
}