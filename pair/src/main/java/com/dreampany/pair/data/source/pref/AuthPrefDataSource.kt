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

    override fun loggedOut(): Boolean {
        return pref.isLoggedOut()
    }

    override suspend fun register(email: String, password: String, name: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun save(user: User): Long {
        TODO("Not yet implemented")
    }
}