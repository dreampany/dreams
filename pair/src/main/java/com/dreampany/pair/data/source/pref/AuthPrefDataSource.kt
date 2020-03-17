package com.dreampany.pair.data.source.pref

import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.AuthDataSource

/**
 * Created by roman on 17/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthPrefDataSource
constructor(
    private val pref: Pref
) : AuthDataSource {

    override fun loggedOut(): Boolean {
        return pref.
    }

    override suspend fun register(email: String, password: String, name: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun save(user: User): Long {
        TODO("Not yet implemented")
    }
}