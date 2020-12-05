package com.dreampany.hello.data.source.firestore

import android.content.Context
import com.dreampany.framework.misc.exts.refId
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.source.api.AuthDataSource
import com.dreampany.hello.data.source.mapper.AuthMapper
import com.dreampany.hello.manager.FirestoreManager
import timber.log.Timber
import java.util.*

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthFirestoreDataSource(
    private val context: Context,
    private val mapper: AuthMapper,
    private val firestore: FirestoreManager
) : AuthDataSource {

    @Throws
    override suspend fun write(input: Auth): Long {
        try {
            val col = Constants.Keys.AUTHS
            firestore.write(col, input.id, input)
            return 1
        } catch (error: Throwable) {
            Timber.e(error)
            return -1
        }
    }

    @Throws
    @Synchronized
    override suspend fun read(id: String): Auth? {
        try {
            val col = Constants.Keys.AUTHS
            val refId = context.refId(id)
            return firestore.read(col, refId, Auth::class)
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }

    @Throws
    override suspend fun read(email: String, password: String): Auth? {
        try {
            val col = Constants.Keys.AUTHS
            val equalTo = TreeMap<String, Any>()
            equalTo.put(Constants.Keys.EMAIL, email)
            equalTo.put(Constants.Keys.PASSWORD, password)
            return firestore.read(col, equalTo, Auth::class)
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }

    @Throws
    override suspend fun readByEmail(email: String): Auth? {
        try {
            val col = Constants.Keys.AUTHS
            val equalTo = TreeMap<String, Any>()
            equalTo.put(Constants.Keys.EMAIL, email)
            return firestore.read(col, equalTo, Auth::class)
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }


}