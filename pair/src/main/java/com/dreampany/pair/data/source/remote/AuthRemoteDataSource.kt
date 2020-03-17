package com.dreampany.pair.data.source.remote

import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.AuthDataSource

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthRemoteDataSource
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

    override suspend fun register(email: String, password: String, name: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun save(user: User): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }


}