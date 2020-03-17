package com.dreampany.pair.data.source.auth

import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.AuthDataSource
import com.google.firebase.auth.FirebaseAuth
import durdinapps.rxfirebase2.RxFirebaseAuth

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthFireauthDataSource
constructor(
    private val mappers: Mappers
) : AuthDataSource {
    override fun setJoinPressed(status: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun isJoinPressed(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setLoggedIn(status: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setLoggedOut(status: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLoggedOut(): Boolean {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): User? {
        return doRegister(email, password, name)
    }

    override suspend fun login(email: String, password: String): User? {
        return doLogin(email, password)
    }

    override suspend fun save(user: User): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    @Throws
    private fun doRegister(
        email: String,
        password: String,
        name: String
    ): User? {
        val auth = FirebaseAuth.getInstance()
        val fbUser =
            RxFirebaseAuth.createUserWithEmailAndPassword(auth, email, password).blockingGet().user
        return if (fbUser != null) mappers.getUser(fbUser, name) else null
    }

    @Throws
    private fun doLogin(
        email: String,
        password: String
    ): User? {
        val auth = FirebaseAuth.getInstance()
        val fbUser =
            RxFirebaseAuth.signInWithEmailAndPassword(auth, email, password).blockingGet().user
        return if (fbUser != null) mappers.getUser(fbUser) else null
    }

}