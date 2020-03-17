package com.dreampany.pair.data.source.pref

import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.AuthDataSource
import com.dreampany.pair.data.source.repo.PrefRepo

/**
 * Created by roman on 17/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthPrefDataSource
constructor(
    private val pref: PrefRepo
) : AuthDataSource {
    override fun setJoinPressed(status: Boolean): Boolean {
        return pref.setJoinPressed(status)
    }

    override fun isJoinPressed(): Boolean {
        return pref.isJoinPressed()
    }

    override fun setLoggedIn(status: Boolean): Boolean {
        return pref.setLoggedIn(status)
    }

    override fun isLoggedIn(): Boolean {
        return pref.isLoggedIn()
    }

    override fun setLoggedOut(status: Boolean): Boolean {
        return pref.setLoggedOut(status)
    }

    override fun isLoggedOut(): Boolean {
        return pref.isLoggedOut()
    }

    override suspend fun login(email: String, password: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun register(email: String, password: String, name: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun save(user: User): Long {
        TODO("Not yet implemented")
    }
}