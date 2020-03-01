package com.dreampany.tools.data.source.firestore

import android.graphics.Bitmap
import com.dreampany.firebase.RxFirebaseFirestore
import com.dreampany.framework.misc.exceptions.WriteException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.framework.data.enums.Source
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.data.source.api.WordDataSource
import com.dreampany.tools.misc.Constants
import com.google.firebase.firestore.FieldPath
import io.reactivex.Maybe

/**
 * Created by roman on 2019-08-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FirestoreWordDataSource(
    private val network: NetworkManager,
    private val firestore: RxFirebaseFirestore
) : WordDataSource {
    override fun track(id: String, weight: Int, source: Source): Long {
        val data =
            hashMapOf(Constants.Firebase.WEIGHT to weight, Constants.Firebase.SOURCE to source.name)
        val error =
            firestore.setItemRx<Map<String, Any>>(Constants.Firebase.TRACK_WORDS, id, data)
                .blockingGet()
        return if (error == null) 0L else -1L
    }

    override fun trackRx(id: String, weight: Int, source: Source): Maybe<Long> {
        return Maybe.create { emitter ->
            val result = track(id, weight, source)
            if (emitter.isDisposed) return@create

            if (result == -1L) {
                emitter.onError(WriteException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    @Throws(Throwable::class)
    override fun getTracks(startAt: String, limit: Long): List<Pair<String, Map<String, Any>>>? {
        return getTracksRx(startAt, limit).blockingGet()
    }

    override fun getTracksRx(startAt: String, limit: Long): Maybe<List<Pair<String, Map<String, Any>>>> {
        return firestore.getDocumentMapsRx(
            Constants.Firebase.TRACK_WORDS,
            orderBy = FieldPath.documentId(),
            ascending = true,
            startAt = startAt,
            limit = limit
        )
    }

    override fun getRawItemsByLength(id: String, limit: Long): List<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isValid(id: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(id: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: Word): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(ids: List<String>): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(ids: List<String>): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(bitmap: Bitmap): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchItems(query: String, limit: Long): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommonItems(): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAlphaItems(): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRawWords(): List<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRawWordsRx(): Maybe<List<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Word): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Word): Long {
        val error = firestore.setItemRx<Word>(Constants.Firebase.WORDS, t.id, t).blockingGet()
        return if (error == null) 0L else -1L
    }

    override fun putItemRx(t: Word): Maybe<Long> {
        return Maybe.create { emitter ->
            val result = putItem(t)
            if (emitter.isDisposed) {
                return@create
            }
            if (result == -1L) {
                emitter.onError(WriteException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun putItems(ts: List<Word>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Word>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: Word): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Word): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Word>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Word>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Throws(Throwable::class)
    override fun getItem(id: String): Word? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Word> {
        return firestore.getItemRx(Constants.Firebase.WORDS, id, Word::class.java)
    }

}