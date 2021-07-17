package com.dreampany.hi.data.source.repo

import com.dreampany.common.inject.qualifier.Nearby
import com.dreampany.common.inject.qualifier.Remote
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.api.UserDataSource
import com.dreampany.network.nearby.core.NearbyApi
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class UserRepo
@Inject constructor(
    @Nearby private val nearby: UserDataSource,
    @Remote private val remote: UserDataSource
) : UserDataSource, UserDataSource.Callback {

    private val callbacks: MutableSet<UserDataSource.Callback>

    init {
        callbacks = Collections.synchronizedSet(HashSet<UserDataSource.Callback>())
    }

    @Throws
    override fun register(callback: UserDataSource.Callback) {
        callbacks.add(callback)
    }

    @Throws
    override fun unregister(callback: UserDataSource.Callback) {
        callbacks.remove(callback)
    }

    @Throws
    override fun startNearby(type: NearbyApi.Type, serviceId: String, user: User) {
        nearby.register(this)
        nearby.startNearby(type, serviceId, user)
    }

    @Throws
    override fun stopNearby() {
        nearby.unregister(this)
        nearby.stopNearby()
    }

    override suspend fun isFavorite(input: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun readFavorites(): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: User): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<User>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun read(id: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(ids: List<String>): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(offset: Long, limit: Long): List<User>? {
        TODO("Not yet implemented")
    }

    override fun onUser(user: User, live: Boolean) {
        // TODO need to save on database
        callbacks.forEach {
            it.onUser(user, live)
        }
    }
}