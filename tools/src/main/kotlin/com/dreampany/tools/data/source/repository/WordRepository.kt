package com.dreampany.tools.data.source.repository

import android.graphics.Bitmap
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.Repository
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.enums.Source
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.api.WordDataSource
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import timber.log.Timber
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
    private val network: NetworkManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: WordMapper,
    @Assets private val assets: WordDataSource,
    @Room private val room: WordDataSource,
    @Firestore private val firestore: WordDataSource,
    @Remote private val remote: WordDataSource,
    @Vision private val vision: WordDataSource
) : Repository<String, Word>(rx, rm), WordDataSource {
    override fun track(id: String, weight: Int, source: Source): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trackRx(id: String, weight: Int, source: Source): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTracks(startAt: String, limit: Long): List<Pair<String, Map<String, Any>>>? {
        return firestore.getTracks(startAt, limit)
    }

    override fun getTracksRx(startAt: String, limit: Long): Maybe<List<Pair<String, Map<String, Any>>>> {
        return firestore.getTracksRx(startAt, limit)
    }

    override fun isValid(id: String): Boolean {
        if (mapper.isExists(id))
            return true
        if (room.isExists(id)) {
            return true
        }
        return assets.isExists(id)
    }

    override fun isExists(id: String): Boolean {
        return room.isExists(id)
    }

    override fun isExists(t: Word): Boolean {
        return room.isExists(t)
    }

    override fun getRawItemsByLength(id: String, limit: Long): List<String>? {
        return room.getRawItemsByLength(id, limit)
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
        return room.getItemsRx()
    }

    override fun getItemsRx(limit: Long): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchItems(query: String, limit: Long): List<Word>? {
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
        return getItemRx(id).blockingGet()
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
            rx.compute(putStoreRx(word, Type.WORD, Subtype.DEFAULT, State.FULL))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            /*rx.compute(removeStoreRx(word, Type.WORD, Subtype.DEFAULT, State.RAW))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())*/
            if (word.hasSynonyms())
                rx.compute(
                    putStoreRx(
                        word,
                        Type.QUIZ,
                        Subtype.SYNONYM,
                        State.DEFAULT
                    )
                ).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            if (word.hasAntonyms())
                rx.compute(
                    putStoreRx(
                        word,
                        Type.QUIZ,
                        Subtype.ANTONYM,
                        State.DEFAULT
                    )
                ).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
        })
        val remoteAny = concatSingleSuccess(remote.getItemRx(id), Consumer { word ->
            if (word.isEmpty()) {
                if (network.hasInternet()) {
                    rx.compute(firestore.trackRx(word.id, word.weight(), Source.WORDNIK))
                        .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
                    rx.compute(putStoreRx(word, Type.WORD, Subtype.DEFAULT, State.ERROR))
                        .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
                    /*rx.compute(removeStoreRx(word, Type.WORD, Subtype.DEFAULT, State.RAW))
                        .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())*/
                }
                return@Consumer
            }

            Timber.v("Remote resolved word %s [%d]", word.id, word.weight())
            rx.compute(mapper.putItemRx(word))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            rx.compute(room.putItemRx(word))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            rx.compute(firestore.putItemRx(word))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            rx.compute(firestore.trackRx(word.id, word.weight(), Source.WORDNIK))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            rx.compute(putStoreRx(word, Type.WORD, Subtype.DEFAULT, State.FULL))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
/*            rx.compute(removeStoreRx(word, Type.WORD, Subtype.DEFAULT, State.RAW))
                .subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())*/
            if (word.hasSynonyms()) rx.compute(
                putStoreRx(
                    word,
                    Type.QUIZ,
                    Subtype.SYNONYM,
                    State.DEFAULT
                )
            ).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())
            if (word.hasAntonyms()) rx.compute(
                putStoreRx(
                    word,
                    Type.QUIZ,
                    Subtype.ANTONYM,
                    State.DEFAULT
                )
            ).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer())

        })
        return concatSingleFirstRx(/*cacheAny,*/ roomAny, firestoreAny, remoteAny)
    }

    /* private */
    private fun getRoomItemRx(id: String): Maybe<Word> {
        return Maybe.create { emitter ->
            val hasFull = storeRepo.isExists(id, Type.WORD, Subtype.DEFAULT, State.FULL)
            var result: Word? = null
            if (hasFull) {
                result = room.getItem(id)
            }
            if (emitter.isDisposed) return@create

            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    fun putStoreRx(word: Word, type: Type, subtype: Subtype, state: State): Maybe<Long> {
        val store = storeMapper.getItem(word.id, type, subtype, state)
        return storeRepo.putItemRx(store)
    }

    fun removeStoreRx(word: Word, type: Type, subtype: Subtype, state: State): Maybe<Int> {
        val store = storeMapper.getItem(word.id, type, subtype, state)
        val result = storeRepo.deleteRx(store)
        return result
    }
}