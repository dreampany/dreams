package com.dreampany.hello.data.source.repo

import com.dreampany.framework.inject.annote.Database
import com.dreampany.framework.inject.annote.Firestore
import com.dreampany.hello.data.model.User
import com.dreampany.hello.data.source.api.UserDataSource
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
class UserRepo
@Inject constructor(
    @Firestore private val firestore: UserDataSource,
    @Database private val database: UserDataSource
) : UserDataSource {

    @Throws
    override suspend fun write(input: User): Long = withContext(Dispatchers.IO) {
        firestore.write(input)
        database.write(input)
    }

    override suspend fun track(id: String): Long {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun read(id: String): User? = withContext(Dispatchers.IO) {
        firestore.read(id)
    }

    @Throws
    override suspend fun onlineIds(limit: Int): List<String>? {
        TODO("Not yet implemented")
    }

    override suspend fun lastUserId(): String? {
        TODO("Not yet implemented")
    }
}