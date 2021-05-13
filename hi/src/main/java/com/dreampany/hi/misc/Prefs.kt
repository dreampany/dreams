package com.dreampany.hi.misc

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext context: Context
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

    fun logIn() {
        setPrivately(Constant.Keys.Pref.LOGGED, true)
    }

    fun logOut() {
        setPrivately(Constant.Keys.Pref.LOGGED, false)
    }

    val isLogged: Boolean
        get() = getPrivately(Constant.Keys.Pref.LOGGED, false)

    fun registerIn() {
        setPrivately(Constant.Keys.Pref.REGISTERED, true)
    }

    fun registerOut() {
        setPrivately(Constant.Keys.Pref.REGISTERED, false)
    }

    val isSigned: Boolean
        get() = getPrivately(Constant.Keys.Pref.REGISTERED, false)

/*    fun write(input: Auth) {
        setPrivately(Constant.Keys.Pref.AUTH, input)
    }

    val auth: Auth?
        get() = getPrivately(Constant.Keys.Pref.AUTH, Auth::class.java, null)

    fun write(input: User) {
        setPrivately(Constant.Keys.Pref.USER, input)
    }

    val user: User?
        get() = getPrivately(Constant.Keys.Pref.USER, User::class.java, null)*/
}