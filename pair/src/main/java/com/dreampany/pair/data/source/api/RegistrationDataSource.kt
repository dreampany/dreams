package com.dreampany.pair.data.source.api

import com.dreampany.pair.data.model.User
import kotlinx.coroutines.Deferred


/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface RegistrationDataSource {
    suspend fun register(
        email: String,
        password: String,
        name: String
    ): User?

    suspend fun save(user: User): Long
}