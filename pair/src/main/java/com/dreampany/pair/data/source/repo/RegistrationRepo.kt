package com.dreampany.pair.data.source.repo

import com.dreampany.common.injector.annote.Auth
import com.dreampany.common.injector.annote.Room
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.RegistrationDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RegistrationRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    //private val network: NetworkManager,
    //private val storeMapper: StoreMapper,
    //private val storeRepo: StoreRepository,
    @Room private val room: RegistrationDataSource,
    @Auth private val auth: RegistrationDataSource
) : RegistrationDataSource {
    override suspend fun register(
        email: String,
        password: String,
        name: String
    ) = withContext(Dispatchers.IO) {
       val remote = auth.register(email, password, name)
        if (remote != null) {
            room.save(remote)
        }
        remote
    }

    override suspend fun save(user: User): Long {
        TODO("Not yet implemented")
    }
}