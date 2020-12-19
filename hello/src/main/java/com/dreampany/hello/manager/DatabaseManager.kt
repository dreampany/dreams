package com.dreampany.hello.manager

import com.dreampany.framework.misc.exts.secondOrNull
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
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

    private val database: FirebaseDatabase

    init {
        database = Firebase.database
        //database.setPersistenceEnabled(true)
    }

    @Synchronized
    fun <T : Any> write(collection: String, document: String, input: T) {
        val colRef = database.getReference(collection)
        val docRef = colRef.child(document)
        Tasks.await(docRef.setValue(input))
    }

    @Synchronized
    fun <T : Any> write(collection: String, document: String, input: Map<String, T>) {
        val colRef = database.getReference(collection)
        val docRef = colRef.child(document)
        Tasks.await(docRef.setValue(input))
    }

    @Synchronized
    fun read(collection: String, document: String): Map<String, Any>? {
        val colRef = database.getReference(collection)
        val docRef = colRef.child(document)
        return read(docRef)
    }

    @Synchronized
    fun read(collection: String, order: String, asc: Boolean): Map<String, Any>? {
        val colRef = database.getReference(collection)
        var queryRef = colRef.orderByChild(order)
        queryRef = if (asc) queryRef.limitToFirst(1) else queryRef.limitToLast(1)
        return read(queryRef)
    }

    @Synchronized
    fun read(ref: DatabaseReference): Map<String, Any>? {
        /* val snapshot: DataSnapshot = null
         if (!snapshot.exists()) return null
         return snapshot.value as Map<String, Any>?*/
        return null
    }

/*    @Synchronized
    fun reads(ref: Query): List<Map<String, Any>>? {
        val source = TaskCompletionSource<DataSnapshot>()
        val task = source.task
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                source.setResult(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                source.setException(error.toException())
            }
        })
        val snapshot: DataSnapshot = Tasks.await(task)
        if (!snapshot.exists()) return null
        val outputs = snapshot.getValue()
        return outputs
    }*/

    @Synchronized
    fun read(ref: Query): Map<String, Any>? {
        val source = TaskCompletionSource<DataSnapshot>()
        val task = source.task
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                source.setResult(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                source.setException(error.toException())
            }
        })
        val snapshot: DataSnapshot = Tasks.await(task)
        if (!snapshot.exists()) return null
        val value = snapshot.value
        if (value is Map<*, *>?) {
            val outputs = value as Map<String, Any>?
            val output = outputs?.values?.firstOrNull() as Map<String, Any>?
            return output
        } else if (value is List<*>?) {
            val outputs = value as List<Any>?
            val output = outputs?.lastOrNull() as Map<String, Any>?
            return output
        }
        return null
    }
}