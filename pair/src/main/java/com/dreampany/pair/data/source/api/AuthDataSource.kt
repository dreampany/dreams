package com.dreampany.pair.data.source.api

import com.dreampany.pair.data.model.User


/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface AuthDataSource {

    fun loggedOut() : Boolean

    @Throws
    suspend fun register(
        email: String,
        password: String,
        name: String
    ): User?

    @Throws
    suspend fun save(user: User): Long
}