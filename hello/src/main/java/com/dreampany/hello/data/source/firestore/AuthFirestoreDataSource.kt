package com.dreampany.hello.data.source.firestore

import com.dreampany.hello.api.ApiConstants
import com.dreampany.hello.api.FirestoreManager
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.source.api.AuthDataSource
import com.dreampany.hello.data.source.mapper.AuthMapper
import timber.log.Timber
import java.util.*

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthFirestoreDataSource(
    private val mapper: AuthMapper,
    private val firestore: FirestoreManager
) : AuthDataSource {

    @Throws
    override suspend fun write(input: Auth): Long {
        try {
            val col = ApiConstants.Keys.AUTHS
            firestore.write(col, input.id, input)
            return 1
        } catch (error: Throwable) {
            Timber.e(error)
            return -1
        }
    }

    @Throws
    override suspend fun read(id: String): Auth? {
        try {
            val col = ApiConstants.Keys.AUTHS
            return firestore.read(col, id, Auth::class)
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }

    @Throws
    override suspend fun read(email: String, password: String): Auth? {
        try {
            val col = ApiConstants.Keys.AUTHS
            val equalTo = TreeMap<String, Any>()
            equalTo.put(ApiConstants.Keys.EMAIL, email)
            equalTo.put(ApiConstants.Keys.PASSWORD, password)
            return firestore.read(col, equalTo, Auth::class)
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }

    @Throws
    override suspend fun readByEmail(email: String): Auth? {
        try {
            val col = ApiConstants.Keys.AUTHS
            val equalTo = TreeMap<String, Any>()
            equalTo.put(ApiConstants.Keys.EMAIL, email)
            return firestore.read(col, equalTo, Auth::class)
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }


}