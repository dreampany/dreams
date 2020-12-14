package com.dreampany.hello.manager

import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 12/15/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class DatabaseManager
@Inject constructor(

) {

    private val database : FirebaseDatabase

    init {
        database = Firebase.database
        database.setPersistenceEnabled(true)
    }

    @Synchronized
    fun <T : Any> write(collection: String, document: String, input: T) {
        val colRef = database.getReference(collection)
        val docRef = colRef.child(document)
        Tasks.await(docRef.setValue(input))
    }
}