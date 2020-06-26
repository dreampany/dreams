package com.dreampany.nearby.data.source.repo

import com.dreampany.framework.inject.annote.Nearby
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.nearby.data.model.User
import com.dreampany.nearby.data.source.api.UserDataSource
import com.dreampany.nearby.data.source.mapper.UserMapper
import com.dreampany.nearby.data.source.pref.AppPref
import com.google.android.gms.nearby.connection.Strategy
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 22/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class UserRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: AppPref,
    private val mapper: UserMapper,
    @Nearby private val nearby: UserDataSource
) : UserDataSource {

    override fun register(callback: UserDataSource.Callback) =
        nearby.register(callback)

    override fun unregister(callback: UserDataSource.Callback) =
        nearby.unregister(callback)

    override fun startNearby(
        strategy: Strategy,
        serviceId: String,
        user: User
    ) {
        nearby.startNearby(strategy, serviceId, user)
    }

    override fun stopNearby() {
        nearby.stopNearby()
    }

    override suspend fun isFavorite(input: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: User): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<User>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(ids: List<String>): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(offset: Long, limit: Long): List<User>? {
        TODO("Not yet implemented")
    }
}