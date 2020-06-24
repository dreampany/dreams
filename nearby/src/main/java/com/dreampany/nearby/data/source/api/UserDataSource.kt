package com.dreampany.nearby.data.source.api

import com.dreampany.nearby.data.model.User
import com.google.android.gms.nearby.connection.Strategy

/**
 * Created by roman on 21/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface UserDataSource {

    interface Callback {
        fun onUser(user: User, live: Boolean)
    }

    fun register(callback: Callback)

    fun unregister(callback: Callback)

    fun startNearby(strategy: Strategy, serviceId: String, user: User)

    fun stopNearby()

    @Throws
    suspend fun isFavorite(input: User): Boolean

    @Throws
    suspend fun toggleFavorite(input: User): Boolean

    @Throws
    suspend fun getFavorites(): List<User>?

    @Throws
    suspend fun put(input: User): Long

    @Throws
    suspend fun put(inputs: List<User>): List<Long>?

    @Throws
    suspend fun get(id: String): User?

    @Throws
    suspend fun gets(): List<User>?

    @Throws
    suspend fun gets(ids: List<String>): List<User>?

    @Throws
    suspend fun gets(offset: Long, limit: Long): List<User>?
}