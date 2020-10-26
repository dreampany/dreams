package com.dreampany.tube.data.source.misc.firestore

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
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
    fun <T : Any> write(collection: String, document: String, input: T) {
        val colRef = firestore.collection(collection)
        val docRef = colRef.document(document)
        Tasks.await(docRef.set(input, SetOptions.merge()))
    }

    @Synchronized
    fun increment(collection: String, document: String, field: String) {
        val colRef = firestore.collection(collection)
        val docRef = colRef.document(document)
        val task = firestore.runTransaction { transition ->
            transition.update(docRef, field, FieldValue.increment(1))
        }
        Tasks.await(task)
    }

    @Synchronized
    fun <T : Any> read(collection: String, document: String, clazz: KClass<T>): T? {
        val ref = firestore.collection(collection).document(document)
        return read(ref, clazz)
    }

    @Synchronized
    fun <T : Any> read(collection: String, equalTo: TreeMap<String, Any>, clazz: KClass<T>): T? {
        val ref = firestore.collection(collection)
        var query: Query = ref
        equalTo.forEach { entry -> query = query.whereEqualTo(entry.key, entry.value) }
        return read(query, clazz)
    }

    @Synchronized
    fun <T : Any> read(ref: DocumentReference, clazz: KClass<T>): T? {
        val snapshot: DocumentSnapshot = Tasks.await(ref.get())
        if (!snapshot.exists()) return null
        return snapshot.toObject(clazz.java)
    }

    @Synchronized
    fun <T : Any> read(ref: Query, clazz: KClass<T>): T? {
        return reads(ref, clazz)?.firstOrNull()
    }

    @Synchronized
    fun <T : Any> reads(ref: Query, clazz: KClass<T>): List<T>? {
        val snapshot: QuerySnapshot = Tasks.await(ref.get())
        if (snapshot.isEmpty) return null
        return snapshot.toObjects(clazz.java)
    }
}