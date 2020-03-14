package com.dreampany.pair.data.source.repo

import com.dreampany.common.injector.annote.Remote
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.RegistrationDataSource
 import kotlinx.coroutines.Deferred
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
    private val mappes: Mappers,
    @Remote private val remote: RegistrationDataSource
) :  RegistrationDataSource {
    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Deferred<User?> {
        return remote.register(email, password, name)
    }

}