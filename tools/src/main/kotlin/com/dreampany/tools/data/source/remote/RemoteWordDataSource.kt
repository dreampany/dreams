package com.dreampany.tools.data.source.remote

import android.graphics.Bitmap
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.wordnik.WordnikManager
import com.dreampany.framework.data.enums.Source
import com.dreampany.tools.data.mapper.WordMapper
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.data.source.api.WordDataSource
import com.dreampany.tools.misc.Constants
import io.reactivex.Maybe
import timber.log.Timber

/**
 * Created by roman on 2019-08-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RemoteWordDataSource(
    private val network: NetworkManager,
    private val mapper: WordMapper,
    private val wordnik: WordnikManager
) : WordDataSource {
    override fun track(id: String, weight: Int, source: Source): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trackRx(id: String, weight: Int, source: Source): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTracks(startAt: String, limit: Long): List<Pair<String, Map<String, Any>>>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTracksRx(startAt: String, limit: Long): Maybe<List<Pair<String, Map<String, Any>>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: Word): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun getItem(id: String): Word? {
        if (network.hasInternet()) {
            val item = wordnik.getWord(id, Constants.Limit.Word.WORD_RESOLVE)
            Timber.v("Wordnik Result %s", if (item == null) "EMPTY" else item.word)
            return mapper.getItem(id, item, true)
        }
        return null
    }

    override fun getItemRx(id: String): Maybe<Word> {
        return Maybe.create { emitter ->
            val result = getItem(id)
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