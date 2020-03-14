package com.dreampany.pair.data.source.remote

import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.source.api.RegistrationDataSource
import com.firebase.ui.auth.data.model.User
import kotlinx.coroutines.Deferred

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RegistrationRemoteDataSource
    (
    mappers: Mappers

    ): RegistrationDataSource {

    override suspend fun isEmpty(): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun putItem(user: User): Deferred<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(id: String): Deferred<User> {
        TODO("Not yet implemented")
    }
}