package com.dreampany.hello.api

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class FirestoreManager
@Inject constructor(

) {

    private val firestore: FirebaseFirestore

    init {
        firestore = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        firestore.firestoreSettings = settings
    }

    @Synchronized
    fun <T : Any> write(collection: String, id: String, input: T) {
        val colRef = firestore.collection(collection)
        val docRef = colRef.document(id)
        Tasks.await(docRef.set(input, SetOptions.merge()))
    }

    @Synchronized
    fun <T : Any> read(ref: DocumentReference, clazz: KClass<T>): T? {
        val snapshot: DocumentSnapshot = Tasks.await(ref.get())
        if (!snapshot.exists()) return null
        return snapshot.toObject(clazz.java)
    }

    @Synchronized
    fun <T : Any> reads(ref: Query, clazz: KClass<T>): List<T>? {
        val snapshot: QuerySnapshot = Tasks.await(ref.get())
        if (snapshot.isEmpty) return null
        return snapshot.toObjects(clazz.java)
    }
}