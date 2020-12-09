package com.dreampany.hello.data.source.firestore

import android.content.Context
import com.dreampany.framework.misc.exts.deviceId
import com.dreampany.framework.misc.exts.refId
import com.dreampany.hello.data.model.User
import com.dreampany.hello.data.source.api.UserDataSource
import com.dreampany.hello.data.source.mapper.UserMapper
import com.dreampany.hello.manager.FirestoreManager
import com.dreampany.hello.misc.Constants
import com.dreampany.hello.misc.map
import com.dreampany.hello.misc.user
import timber.log.Timber

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserFirestoreDataSource(
    private val context: Context,
    private val mapper: UserMapper,
    private val firestore: FirestoreManager
) : UserDataSource {

    @Throws
    override suspend fun write(input: User): Long {
        try {
            val col = Constants.Keys.Firestore.USERS
            val refId = context.refId(input.id)
            val input = input.map
            firestore.write(col, refId, input)
            return 0
        } catch (error: Throwable) {
            Timber.e(error)
            return -1
        }
    }

    @Throws
    override suspend fun read(id: String): User? {
        try {
            val col = Constants.Keys.Firestore.USERS
            val refId = context.refId(id)
            val deviceId = context.deviceId
            val output = firestore.read(col, refId)
            return output?.user
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }
}