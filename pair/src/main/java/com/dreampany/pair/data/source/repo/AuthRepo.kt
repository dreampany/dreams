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
    @Fireauth private val fireauth: AuthDataSource,
    @Firestore private val firestore: AuthDataSource
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
        val user = fireauth.register(email, password, name)
        if (user != null) {
            room.save(user)
            firestore.save(user)
        }
        user
    }

    @Throws
    override suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        val user = fireauth.login(email, password)
        var fullUser: User? = null
        if (user != null) {
            fullUser = room.getUserByEmail(email)
            if (fullUser == null || fullUser.id.isEmpty()) {
                fullUser = firestore.getUserByEmail(email)
            }
        }
        fullUser
    }


    @Throws
    override suspend fun save(user: User): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }
}