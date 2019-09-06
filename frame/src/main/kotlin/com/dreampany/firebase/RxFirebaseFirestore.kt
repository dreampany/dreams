package com.dreampany.firebase

import com.google.firebase.firestore.*
import io.reactivex.Completable
import io.reactivex.Maybe
import org.apache.commons.lang3.tuple.MutablePair
import org.apache.commons.lang3.tuple.MutableTriple
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-09-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RxFirebaseFirestore @Inject constructor() {

    private val firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder().build()
        firestore.firestoreSettings = settings
    }

    /**
     * @param collection
     * @param document
     * @param item
     * @param <T>
     * @return
    </T> */
    fun <T> setItemRx(
        collection: String,
        document: String,
        item: T
    ): Completable {
        return setItemRx(collection, null, document, item)
    }

    /**
     * @param collection
     * @param internalPaths collection of internal paths containing MutablePair<Document></Document>, Collection>
     * @param document
     * @param item
     * @param <T>
     * @return
    </T> */
    fun <T> setItemRx(
        collection: String,
        internalPaths: TreeSet<MutablePair<String, String>>?,
        document: String,
        item: T
    ): Completable {
        var ref = firestore.collection(collection)
        if (internalPaths != null && !internalPaths.isEmpty()) {
            for (path in internalPaths) {
                if (path.left != null && path.right != null) {
                    ref = ref.document(path.left).collection(path.right)
                }
            }
        }
        val doc = ref.document(document)
        return setItemRx(doc, item)
    }

    fun <T> setItemsRx(
        collection: String,
        items: Map<String, MutableTriple<String, String, T>>
    ): Completable {

        val ref = firestore.collection(collection)
        val batch = firestore.batch()
        for ((document, item) in items) {
            val childCollection = ref.document(item.left).collection(item.middle)
            val doc = childCollection.document(document)
            batch.set(doc, item.right!!, SetOptions.merge())
        }
        return atomicOperation<Any>(batch)
    }

    fun <T> setItemRx(
        ref: DocumentReference,
        item: T
    ): Completable {
        return Completable.create { emitter ->
            RxCompletableHandler.assignOnTask(
                emitter,
                ref.set(item!!, SetOptions.merge())
            )
        }
    }

    fun <T> atomicOperation(batch: WriteBatch): Completable {
        return Completable.create { emitter ->
            RxCompletableHandler.assignOnTask(
                emitter,
                batch.commit()
            )
        }
    }

    fun <T> getItemRx(
        collectionPath: String,
        documentPath: String,
        clazz: Class<T>
    ): Maybe<T> {
        val ref = firestore.collection(collectionPath).document(documentPath)
        return getItemRx(ref, clazz)
    }

    fun <T> getItemRx(
        collection: String,
        internalPaths: TreeSet<MutablePair<String, String>>?,
        equalTo: List<MutablePair<String, Any>>?,
        lessThanOrEqualTo: List<MutablePair<String, Any>>?,
        greaterThanOrEqualTo: List<MutablePair<String, Any>>?,
        clazz: Class<T>
    ): Maybe<T> {

        var ref = firestore.collection(collection)
        if (internalPaths != null && !internalPaths.isEmpty()) {
            for (path in internalPaths) {
                if (path.left != null && path.right != null) {
                    ref = ref.document(path.left).collection(path.right)
                }
            }
        }

        var query: Query = ref
        if (equalTo != null) {
            for ((key, value) in equalTo) {
                query = query.whereEqualTo(key, value)
            }
        }
        if (lessThanOrEqualTo != null) {
            for ((key, value) in lessThanOrEqualTo) {
                query = query.whereLessThanOrEqualTo(key, value)
            }
        }
        if (greaterThanOrEqualTo != null) {
            for ((key, value) in greaterThanOrEqualTo) {
                query = query.whereGreaterThanOrEqualTo(key, value)
            }
        }
        return getItemRx(ref, clazz)
    }

    fun <T> getItemsRx(
        collection: String,
        internalPaths: TreeSet<MutablePair<String, String>>?,
        equalTo: List<MutablePair<String, Any>>?,
        lessThanOrEqualTo: List<MutablePair<String, Any>>?,
        greaterThanOrEqualTo: List<MutablePair<String, Any>>?,
        clazz: Class<T>
    ): Maybe<List<T>> {

        var ref = firestore.collection(collection)
        if (internalPaths != null && !internalPaths.isEmpty()) {
            for (path in internalPaths) {
                if (path.left != null && path.right != null) {
                    ref = ref.document(path.left).collection(path.right)
                }
            }
        }

        var query: Query = ref
        if (equalTo != null) {
            for ((key, value) in equalTo) {
                query = query.whereEqualTo(key, value)
            }
        }
        if (lessThanOrEqualTo != null) {
            for ((key, value) in lessThanOrEqualTo) {
                query = query.whereLessThanOrEqualTo(key, value)
            }
        }
        if (greaterThanOrEqualTo != null) {
            for ((key, value) in greaterThanOrEqualTo) {
                query = query.whereGreaterThanOrEqualTo(key, value)
            }
        }
        return getItemsRx(ref, clazz)
    }

    fun <T> getItemRx(ref: DocumentReference, clazz: Class<T>): Maybe<T> {
        return Maybe.create { emitter ->
            ref.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    emitter.onSuccess(snapshot.toObject(clazz)!!)
                } else {
                    emitter.onComplete()
                }
            }.addOnFailureListener { e ->
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            }
        }
    }

    fun <T> getItemRx(ref: Query, clazz: Class<T>): Maybe<T> {
        return Maybe.create { emitter ->
            ref.get().addOnSuccessListener { snapshot ->
                if (emitter.isDisposed) {
                    return@addOnSuccessListener
                }
                if (snapshot.isEmpty) {
                    emitter.onComplete()
                } else {
                    emitter.onSuccess(snapshot.documents[0].toObject(clazz)!!)
                }
            }.addOnFailureListener { error ->
                if (emitter.isDisposed()) {
                    return@addOnFailureListener
                }
                emitter.onError(error)
            }
        }
    }

    fun <T> getItemsRx(ref: Query, clazz: Class<T>): Maybe<List<T>> {
        return Maybe.create { emitter ->
            ref.get().addOnSuccessListener { snapshot ->
                if (emitter.isDisposed) {
                    return@addOnSuccessListener
                }
                if (snapshot.isEmpty) {
                    emitter.onComplete()
                } else {
                    emitter.onSuccess(snapshot.toObjects(clazz))
                }
            }.addOnFailureListener { error ->
                if (emitter.isDisposed()) {
                    return@addOnFailureListener
                }
                emitter.onError(error)
            }
        }
    }
}