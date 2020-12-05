package com.dreampany.hello.data.source.repo

import com.dreampany.framework.inject.annote.Firestore
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.source.api.AuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AuthRepo
@Inject constructor(
    @Firestore private val firestore: AuthDataSource
) : AuthDataSource {

    @Throws
    @Synchronized
    override suspend fun write(input: Auth) = withContext(Dispatchers.IO) {
        firestore.write(input)
    }

    @Throws
    @Synchronized
    override suspend fun read(id: String) = withContext(Dispatchers.IO) {
        firestore.read(id)
    }

    @Throws
    @Synchronized
    override suspend fun read(email: String, password: String) = withContext(Dispatchers.IO) {
        firestore.read(email, password)
    }

    @Throws
    @Synchronized
    override suspend fun readByEmail(email: String) = withContext(Dispatchers.IO) {
        firestore.readByEmail(email)
    }
}