package com.dreampany.pair.data.source.remote

import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.source.api.RegistrationDataSource
import kotlinx.coroutines.Deferred

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RegistrationRemoteDataSource
constructor(
   val mappers: Mappers
) : RegistrationDataSource {
    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Deferred<com.dreampany.pair.data.model.User?> {
        TODO("Not yet implemented")
    }


}