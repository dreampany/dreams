package com.dreampany.tools.data.source.repository

import android.graphics.Bitmap
import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.source.repository.Repository
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.api.WordDataSource
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WordRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val mapper: WordMapper,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    @Assets private val assets: WordDataSource,
    @Room private val room: WordDataSource,
    @Firestore private val firestore: WordDataSource,
    @Remote private val remote: WordDataSource,
    @Vision private val vision: WordDataSource
) : Repository<String, Word>(rx, rm), WordDataSource {
    override fun isExists(id: String): Boolean {
        return room.isExists(id)
    }

    override fun isExists(t: Word): Boolean {
        return room.isExists(t)
    }

    override fun getItems(ids: List<String>): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Int): List<Word>? {
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

    override fun getItemsRx(limit: Int): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchItems(query: String, limit: Int): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommonItems(): List<Word>? {
        return assets.getCommonItems()
    }

    override fun getAlphaItems(): List<Word>? {
        return assets.getAlphaItems()
    }

    override fun getRawWords(): List<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRawWordsRx(): Maybe<List<String>> {
        return room.getRawWordsRx()
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: Word): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Word>): List<Long>? {
        return room.putItems(ts)
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

    override fun getItem(id: String): Word? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Word> {
        val cacheAny = mapper.getItemRx(id)
        val roomAny = concatSingleSuccess(getRoomItemRx(id), Consumer { word ->
            rx.compute(mapper.putItemRx(word))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
        })
        val firestoreAny = concatSingleSuccess(firestore.getItemRx(id), Consumer { word ->
            rx.compute(mapper.putItemRx(word))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            rx.compute(room.putItemRx(word))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
        })
        val remoteAny = concatSingleSuccess(remote.getItemRx(id), Consumer { word ->
            rx.compute(mapper.putItemRx(word))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            rx.compute(room.putItemRx(word))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            rx.compute(firestore.putItemRx(word))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
        })
        return concatSingleFirstRx(/*cacheAny,*/ roomAny, firestoreAny, remoteAny)
    }

    /* private */
    private fun getRoomItemRx(id: String): Maybe<Word> {
        return Maybe.create{emitter ->
           val hasFull = storeRepo.isExists(id, Type.WORD, Subtype.DEFAULT, State.FULL)
            var result : Word? = null
            if (hasFull) {
                result = room.getItem(id)
            }
            if (emitter.isDisposed) {
                return@create
            }
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }
}