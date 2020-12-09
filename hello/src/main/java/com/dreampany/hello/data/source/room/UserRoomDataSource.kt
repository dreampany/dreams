package com.dreampany.hello.data.source.room

import com.dreampany.hello.data.model.User
import com.dreampany.hello.data.source.api.UserDataSource
import com.dreampany.hello.data.source.mapper.UserMapper
import com.dreampany.hello.data.source.room.dao.UserDao

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserRoomDataSource(
    private val mapper: UserMapper,
    private val dao: UserDao
) : UserDataSource {

    @Throws
    override suspend fun write(input: User): Long {
        return dao.insertOrReplace(input)
    }

    override suspend fun read(id: String): User? {
        TODO("Not yet implemented")
    }
}