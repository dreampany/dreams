package com.dreampany.pair.data.source.repo

import com.dreampany.common.injector.annote.*
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.AuthDataSource
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
class AuthRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    //private val network: NetworkManager,
    //private val storeMapper: StoreMapper,
    //private val storeRepo: StoreRepository,
    @Pref private val pref: AuthDataSource,
    @Room private val room: AuthDataSource,
    @Fireauth private val auth: AuthDataSource
) : AuthDataSource {
    override fun setJoinPressed(status: Boolean): Boolean {
        return pref.setJoinPressed(status)
    }

    override fun isJoinPressed(): Boolean {
        return pref.isJoinPressed()
    }

    override fun setLoggedIn(status: Boolean): Boolean {
        return pref.setLoggedIn(status)
    }

    override fun isLoggedIn(): Boolean {
        return pref.isLoggedIn()
    }

    override fun setLoggedOut(status: Boolean): Boolean {
        return pref.setLoggedOut(status)
    }

    override fun isLoggedOut(): Boolean {
        return pref.isLoggedOut()
    }

    @Throws
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

    @Throws
    override suspend fun save(user: User): Long {
        TODO("Not yet implemented")
    }
}