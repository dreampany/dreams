package com.dreampany.hello.data.source.database

import android.content.Context
import com.dreampany.framework.misc.exts.ref
import com.dreampany.hello.data.model.User
import com.dreampany.hello.data.source.api.UserDataSource
import com.dreampany.hello.data.source.mapper.UserMapper
import com.dreampany.hello.manager.DatabaseManager
import com.dreampany.hello.misc.*
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

    @Throws
    override suspend fun write(id: String): Long {
        try {
            val col = context.ref(Constants.Keys.Firebase.USERS)
            val input = id.writeMap()
            database.write(col, id, input)
            return 0
        } catch (error: Throwable) {
            Timber.e(error)
            return -1
        }
    }

    @Throws
    override suspend fun track(id: String, index : Long): Long {
        try {
            val col = context.ref(Constants.Keys.Firebase.USERS)
            val input = id.trackMap(index)
            database.write(col, id, input)
            return 0
        } catch (error: Throwable) {
            Timber.e(error)
            return -1
        }
    }

    override suspend fun read(id: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun read(ids: List<String>): List<User>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun newIds(limit: Int): List<String>? {
        try {
            val col = context.ref(Constants.Keys.Firebase.USERS)
            val order = Constants.Keys.Firebase.CREATED_AT
            val asc = false
            val output = database.reads(col, order, asc, limit)
            if (output == null) return null
           /* val id = output.id
            mapper.writeIndex(id, output.index)
            mapper.writeTimestamp(id, output.time)
            mapper.writeOnline(id, output.online)*/
            return null
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }

    override suspend fun onlineIds(limit: Int): List<String>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun lastId(): String? {
        try {
            val col = context.ref(Constants.Keys.Firebase.USERS)
            val order = Constants.Keys.Firebase.ID
            val asc = false
            val output = database.read(col, order, asc)
            if (output == null) return null
            val id = output.id
            mapper.writeIndex(id, output.index)
            mapper.writeTimestamp(id, output.time)
            mapper.writeOnline(id, output.online)
            return id
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }

}