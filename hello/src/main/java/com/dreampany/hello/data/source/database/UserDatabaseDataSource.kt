package com.dreampany.hello.data.source

import android.content.Context
import com.dreampany.framework.misc.exts.ref
import com.dreampany.hello.data.model.User
import com.dreampany.hello.data.source.api.UserDataSource
import com.dreampany.hello.data.source.mapper.UserMapper
import com.dreampany.hello.manager.DatabaseManager
import com.dreampany.hello.misc.Constants
import com.dreampany.hello.misc.map
import timber.log.Timber

/**
 * Created by roman on 12/14/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserDatabaseDataSource(
    private val context: Context,
    private val mapper: UserMapper,
    private val database: DatabaseManager
) : UserDataSource {

    @Throws
    override suspend fun write(input: User): Long {
        try {
            val col = context.ref(Constants.Keys.Firebase.USERS)
            val refId = input.id
            val input = input.map
            database.write(col, refId, input)
            return 0
        } catch (error: Throwable) {
            Timber.e(error)
            return -1
        }
    }

    override suspend fun track(id: String): Long {
        TODO("Not yet implemented")
    }

    override suspend fun read(id: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun onlineIds(limit: Int): List<String>? {
        TODO("Not yet implemented")
    }

    override suspend fun lastUserId(): String? {
        TODO("Not yet implemented")
    }

}