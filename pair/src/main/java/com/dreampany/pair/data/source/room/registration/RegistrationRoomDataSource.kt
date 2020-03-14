package com.dreampany.pair.data.source.room.registration

import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.RegistrationDataSource
import com.dreampany.pair.data.source.room.dao.UserDao

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RegistrationRoomDataSource
constructor(
    private val dao: UserDao
) : RegistrationDataSource {
    override suspend fun register(email: String, password: String, name: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun save(user: User): Long {
        return dao.insertOrReplace(user)
    }
}