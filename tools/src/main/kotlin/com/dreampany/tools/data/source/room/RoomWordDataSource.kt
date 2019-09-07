package com.dreampany.tools.data.source.room

import android.graphics.Bitmap
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.misc.exception.WriteException
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.api.WordDataSource
import com.dreampany.tools.data.source.room.dao.AntonymDao
import com.dreampany.tools.data.source.room.dao.SynonymDao
import com.dreampany.tools.data.source.room.dao.WordDao
import io.reactivex.Maybe
import timber.log.Timber

/**
 * Created by roman on 2019-08-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RoomWordDataSource(
    private val mapper: WordMapper,
    private val dao: WordDao,
    private val synonymDao: SynonymDao,
    private val antonymDao: AntonymDao
) : WordDataSource {
    override fun track(word: Word): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trackRx(word: Word): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTracks(startAt: Int, limit: Int): List<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTracksRx(startAt: Int, limit: Int): Maybe<List<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRawItemsByLength(id: String, limit: Int): List<String>? {
        return dao.getRawItemsByLength(id, limit)
    }

    override fun isValid(id: String): Boolean {
        return isExists(id)
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
        return dao.getItemsRx()
    }

    override fun getItemsRx(limit: Int): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchItems(query: String, limit: Int): List<Word>? {
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
        return dao.getRawItemsRx()
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
        val result = dao.insertOrReplace(t)
        if (result != -1L) {
            val synonyms = mapper.getSynonyms(t)
            val antonyms = mapper.getAntonyms(t)
            if (!synonyms.isNullOrEmpty()) {
                val synonymResults = synonymDao.insertOrReplace(synonyms)
                Timber.v("Synonym Stored %d", synonymResults.size)
            }
            if (!antonyms.isNullOrEmpty()) {
                val antonymResults = antonymDao.insertOrReplace(antonyms)
                Timber.v("Antonym Stored %d", antonymResults.size)
            }
        }
        return result
    }

    override fun putItemRx(t: Word): Maybe<Long> {
        return Maybe.create { emitter ->
            val result = putItem(t)
            if (emitter.isDisposed) return@create
            emitter.onSuccess(result)
        }
    }

    override fun putItems(ts: List<Word>): List<Long>? {
        val result = dao.insertOrIgnore(ts)
        return result
    }

    override fun putItemsRx(ts: List<Word>): Maybe<List<Long>> {
        return Maybe.create { emitter ->
            val result = putItems(ts)
            if (emitter.isDisposed) return@create
            if (result.isNullOrEmpty()) {
                emitter.onError(WriteException())
            } else {
                emitter.onSuccess(result)
            }
        }
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
        val result = dao.getItem(id)
        result?.run {
            val synonyms = synonymDao.getItems(this.id)
            val antonyms = antonymDao.getItems(this.id)
            result.synonyms = mapper.getSynonyms(this, synonyms)
            result.antonyms = mapper.getAntonyms(this, antonyms)
        }
        return result
    }

    override fun getItemRx(id: String): Maybe<Word> {
        return Maybe.create { emitter ->
            val result = getItem(id)
            if (emitter.isDisposed) return@create
            if (result == null) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

}