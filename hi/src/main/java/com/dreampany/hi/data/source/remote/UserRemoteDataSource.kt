package com.dreampany.hi.data.source.remote

import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.api.UserDataSource
import com.dreampany.network.nearby.core.NearbyApi

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class UserRemoteDataSource() : UserDataSource {

    override fun register(callback: UserDataSource.Callback) {
        TODO("Not yet implemented")
    }

    override fun unregister(callback: UserDataSource.Callback) {
        TODO("Not yet implemented")
    }

    override fun startNearby(type: NearbyApi.Type, serviceId: String, user: User) {
        TODO("Not yet implemented")
    }

    override fun stopNearby() {
        TODO("Not yet implemented")
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
}