package com.dreampany.hi.misc

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 5/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class Prefs
@Inject constructor(
    context: Context
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constant.Keys.Pref.PREF

    fun start() {
        setPrivately(Constant.Keys.Pref.STARTED, true)
    }

    fun stop() {
        setPrivately(Constant.Keys.Pref.STARTED, false)
    }

    val isStarted: Boolean
        get() = getPrivately(Constant.Keys.Pref.STARTED, false)

    fun login() {
        setPrivately(Constant.Keys.Pref.LOGGED, true)
    }

    fun logout() {
        setPrivately(Constant.Keys.Pref.LOGGED, false)
    }

    val isLogged: Boolean
        get() = getPrivately(Constant.Keys.Pref.LOGGED, false)

    fun signIn() {
        setPrivately(Constant.Keys.Pref.SIGN_IN, true)
    }

    fun signOut() {
        setPrivately(Constant.Keys.Pref.SIGN_IN, false)
    }

    val isSignIn: Boolean
        get() = getPrivately(Constant.Keys.Pref.SIGN_IN, false)

    fun write(input: Auth) {
        setPrivately(Constant.Keys.Pref.AUTH, input)
    }

    val auth: Auth?
        get() = getPrivately(Constant.Keys.Pref.AUTH, Auth::class.java, null)

    fun write(input: User) {
        setPrivately(Constant.Keys.Pref.USER, input)
    }

    val user: User?
        get() = getPrivately(Constant.Keys.Pref.USER, User::class.java, null)
}