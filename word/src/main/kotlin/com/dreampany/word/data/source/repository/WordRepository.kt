package com.dreampany.word.data.source.repository

import android.graphics.Bitmap
import com.dreampany.frame.data.source.repository.RepositoryKt
import com.dreampany.frame.misc.*
import com.dreampany.frame.util.DataUtil
import com.dreampany.word.data.misc.WordMapper
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.api.WordDataSource
import com.dreampany.word.data.source.pref.Pref
import io.reactivex.Maybe
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WordRepository @Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    val pref: Pref,
    val mapper: WordMapper,
    @Assets val assets: WordDataSource,
    @Room val room: WordDataSource,
    @Firestore val firestore: WordDataSource,
    @Remote val remote: WordDataSource,
    @Vision val vision: WordDataSource
) : RepositoryKt<String, Word>(rx, rm), WordDataSource {
    override fun isExists(word: String): Boolean {
        return assets.isExists(word)
    }

    override fun getTodayItem(): Word? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTodayItemRx(): Maybe<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(word: String, full: Boolean): Word? {
        var result = room.getItem(word, full)
        if (result == null) {
            result = assets.getItem(word, full)
        }
        if (result == null) {

        }
        return result
    }

    override fun deleteRx(t: Word): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Word? {
        return room.getItem(id)
    }

    override fun getItemRx(word: String, full: Boolean): Maybe<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(ids: List<String>): List<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Int): List<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(ids: List<String>): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(bitmap: Bitmap): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Word>> {
        val assets = getAssetsItemsIfRx()
        val room = this.room.getItemsRx()
        return concatLastRx(assets, room)
    }

    override fun getItemsRx(limit: Int): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchItems(query: String, limit: Int): List<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommonItems(): List<Word>? {
        return assets.getCommonItems();
    }

    override fun getAlphaItems(): List<Word>? {
        return assets.getAlphaItems()
    }

    override fun getRawWords(): List<String>? {
        return room.getRawWords()
    }

    override fun getRawWordsRx(): Maybe<List<String>> {
        val assets = this.assets.getRawWordsRx()
        val room = this.room.getRawWordsRx()
        return concatFirstOfStringRx(room, assets)
    }

    override fun isEmpty(): Boolean {
        return room.isEmpty()
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        return Maybe.fromCallable({ this.isEmpty() })
    }

    override fun getCount(): Int {
        return room.getCount()
    }

    override fun getCountRx(): Maybe<Int> {
        return room.getCountRx()
    }

    override fun isExists(t: Word): Boolean {
        var exists = room.isExists(t)
        if (!exists) {
            exists = assets.isExists(t)
        }
        return exists
    }

    override fun isExistsRx(t: Word): Maybe<Boolean> {
        return Maybe.fromCallable { isExists(t) }
    }

    override fun putItem(t: Word): Long {
        return room.putItem(t)
    }

    override fun putItemRx(t: Word): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Word>): List<Long>? {
        return room.putItems(ts)
    }

    override fun putItemsRx(ts: List<Word>): Maybe<List<Long>> {
        return room.putItemsRx(ts)
    }

    override fun delete(t: Word): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Word>): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Word>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getRoomItem(word: String, full: Boolean): Word? {
        return room.getItem(word, full)
    }

    fun getFirestoreItem(word: String, full: Boolean): Word? {
        return firestore.getItem(word, full)
    }

    fun getRemoteItem(word: String, full: Boolean): Word? {
        return remote.getItem(word, full)
    }

    fun putRoomItem(word: Word): Long {
        return room.putItem(word)
    }

    fun putFirestoreItem(word: Word): Long {
        return firestore.putItem(word)
    }

    fun putRemoteItem(word: Word): Long {
        return remote.putItem(word)
    }

    fun getItemOfMapper(word: String): Word? {
        return mapper.toItem(word)
    }

    fun getItemOf(word: String, full: Boolean): Word? {
        var result = room.getItem(word, full)
        if (result == null) {
            result = getItemOfMapper(word)
        }
        return result
    }

    /* private api */
    private fun getAssetsItemsIfRx(): Maybe<List<Word>> {
        return Maybe.fromCallable {
            if (getCount() == 0) {
                val maybe = saveRoomOfItems(assets.getItemsRx())
                maybe.blockingGet()
            }
            null
        }
    }

/*    private fun getItemsByStatesRx(items: List<State>): Maybe<List<Word>> {
        return Flowable.fromIterable(items)
            .map { item -> mapper.toItem(item, room) }
            .toList()
            .toMaybe()
    }*/


    private fun saveRoomOfItems(source: Maybe<List<Word>>): Maybe<List<Word>> {
        return source
            .filter { items -> !DataUtil.isEmpty(items) }
            .doOnSuccess { words -> rx.compute(putItemsRx(words as ArrayList<Word>)).subscribe() }
    }

    /*    private Maybe<Word> getTodayFromRoom() {
        String type = ItemType.WORD.name();
        String subtype = ItemSubtype.DEFAULT.name();
        String state = ItemState.TODAY.name();
        long from = TimeUtil.startOfDay();
        long to = TimeUtil.endOfDay() - 1;
        return stateRepo.getItemOrderByRx(type, subtype, state, from, to)
                .map(item -> {
                    if (item != null) {
                        return mapper.toItem(item, room);
                    }
                    return null;
                });
    }*/

/*    private Maybe<Word> generateToday() {
        String type = ItemType.WORD.name();
        String subtype = ItemSubtype.DEFAULT.name();
        String state = ItemState.TODAY.name();
        return stateRepo.getItemNotStateOrderByRx(type, subtype, state)
                .map(item -> {
                    if (item != null) {
                        return mapper.toItem(item, room);
                    }
                    return null;
                });
    }*/
}