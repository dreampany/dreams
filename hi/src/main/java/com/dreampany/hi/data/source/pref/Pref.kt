package com.dreampany.hi.data.source.pref
import com.dreampany.common.data.source.pref.Pref
import android.content.Context
import com.dreampany.hi.data.model.Auth
import com.dreampany.hi.data.model.User
import com.dreampany.hi.misc.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref @Inject constructor(
   @ApplicationContext context: Context
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.PREF

    fun start() {
        setPrivately(Constants.Keys.Pref.STARTED, true)
    }

    fun stop() {
        setPrivately(Constants.Keys.Pref.STARTED, false)
    }

    val isStarted: Boolean
        get() = getPrivately(Constants.Keys.Pref.STARTED, false)

    fun login() {
        setPrivately(Constants.Keys.Pref.LOGGED, true)
    }

    fun logout() {
        setPrivately(Constants.Keys.Pref.LOGGED, false)
    }

    val isLogged: Boolean
        get() = getPrivately(Constants.Keys.Pref.LOGGED, false)

    fun signIn() {
        setPrivately(Constants.Keys.Pref.SIGN_IN, true)
    }

    fun signOut() {
        setPrivately(Constants.Keys.Pref.SIGN_IN, false)
    }

    val isSignIn: Boolean
        get() = getPrivately(Constants.Keys.Pref.SIGN_IN, false)

    fun write(input: Auth) {
        setPrivately(Constants.Keys.Pref.AUTH, input)
    }

    val auth: Auth?
        get() = getPrivately(Constants.Keys.Pref.AUTH, Auth::class.java, null)

/*    fun write(input: User) {
        setPrivately(Constants.Keys.Pref.USER, input)
    }*/

    var user: User?
        get() = getPrivately(Constants.Keys.Pref.USER, User::class.java, null)
        set(value) { setPrivately(Constants.Keys.Pref.USER, value) }
}