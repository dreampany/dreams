package com.dreampany.pair.data.source

import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.RegistrationDataSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RegistrationAuthDataSource(
    val mappers: Mappers
) : RegistrationDataSource {
    override suspend fun register(
        email: String,
        password: String,
        name: String
        ) = withContext(Dispatchers.IO) {
        async {
            doRegister(name, email, password)
        }
    }

    private fun doRegister(
        email: String,
        password: String,
        name: String
    ): User? {
        val auth = FirebaseAuth.getInstance()
        val fbUser = auth.signInWithEmailAndPassword(email, password).result?.user
        return if (fbUser != null) mappers.getUser(fbUser, name) else null
    }

}