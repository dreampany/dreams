package com.dreampany.hello.data.source.firestore

import com.dreampany.hello.api.ApiConstants
import com.dreampany.hello.api.FirestoreManager
import com.dreampany.hello.data.model.User
import com.dreampany.hello.data.source.api.UserDataSource
import com.dreampany.hello.data.source.mapper.UserMapper

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
        val col = ApiConstants.Keys.USERS
        firestore.write(col, input.id, input)
        return 1
    }
}