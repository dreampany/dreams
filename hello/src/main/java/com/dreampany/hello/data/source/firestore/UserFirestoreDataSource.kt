package com.dreampany.hello.data.source.firestore

import com.dreampany.hello.data.model.User
import com.dreampany.hello.data.source.api.UserDataSource
import com.dreampany.hello.data.source.mapper.UserMapper
import com.dreampany.hello.manager.FirestoreManager
import timber.log.Timber

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserFirestoreDataSource(
    private val mapper: UserMapper,
    private val firestore: FirestoreManager
) : UserDataSource {

    @Throws
    override suspend fun write(input: User): Long {
        try {
            val col = Constants.Keys.USERS
            firestore.write(col, input.id, input)
            return 1
        } catch (error: Throwable) {
            Timber.e(error)
            return -1
        }
    }
}