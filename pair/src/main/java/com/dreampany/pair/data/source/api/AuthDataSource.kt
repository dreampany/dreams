package com.dreampany.pair.data.source.api

import com.dreampany.pair.data.model.User


/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface AuthDataSource {

    fun setJoinPressed(status: Boolean) : Boolean

    fun isJoinPressed() : Boolean

    fun setLoggedIn(status: Boolean) : Boolean

    fun isLoggedIn() : Boolean

    fun setLoggedOut(status: Boolean) : Boolean

    fun isLoggedOut() : Boolean

    @Throws
    suspend fun register(
        email: String,
        password: String,
        name: String
    ): User?

    @Throws
    suspend fun login(
        email: String,
        password: String
    ): User?

    @Throws
    suspend fun save(user: User): Long

    @Throws
    suspend fun getUserByEmail(email: String): User?
}