package com.dreampany.pair.data.source.store

import android.content.Context
import com.dreampany.common.misc.constant.Constants
import com.dreampany.pair.data.mapper.Mappers
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.api.AuthDataSource
import com.dreampany.pair.misc.constant.AppConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import durdinapps.rxfirebase2.RxFirestore

/**
 * Created by roman on 17/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthFirestoreDataSource
constructor(
    private val context: Context,
    private val mappers: Mappers
) : AuthDataSource {

    private val firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder().build()
        firestore.firestoreSettings = settings
    }

    override fun setJoinPressed(status: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun isJoinPressed(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setLoggedIn(status: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setLoggedOut(status: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLoggedOut(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun register(email: String, password: String, name: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun save(user: User): Long {
        val document =
            firestore.collection(Constants.Keys.Firestore.PACKAGES).document(context.packageName)
        val userDocument = document.collection(AppConstants.Keys.Firestore.USERS).document(user.id)
        val error = RxFirestore.setDocument(userDocument, user, SetOptions.merge()).blockingGet()
        return if (error == null) -1L else 0L
    }

    override suspend fun getUserByEmail(email: String): User? {
        val document =
            firestore.collection(Constants.Keys.Firestore.PACKAGES).document(context.packageName)
        val users = document.collection(AppConstants.Keys.Firestore.USERS)
        val userQuery = users.whereEqualTo(AppConstants.Keys.Firestore.EMAIL, email).limit(1L)
        val user = RxFirestore.getCollection(userQuery, User::class.java).blockingGet().first()
        return user
    }
}