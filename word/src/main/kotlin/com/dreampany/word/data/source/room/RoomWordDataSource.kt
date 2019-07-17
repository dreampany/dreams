package com.dreampany.word.data.source.room

import android.graphics.Bitmap
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.util.DataUtil
import com.dreampany.word.data.misc.WordMapper
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.api.WordDataSource
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RoomWordDataSource(
    val mapper: WordMapper,
    val dao: WordDao,
    val synonymDao: SynonymDao,
    val antonymDao: AntonymDao
) : WordDataSource {
    override fun getTodayItem(): Word {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTodayItemRx(): Maybe<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(word: String, full: Boolean): Word? {
        val result = dao.getItem(word)
        if (result != null && full) {
            val synonyms = synonymDao.getItems(result.id)
            val antonyms = antonymDao.getItems(result.id)
            result.synonyms = mapper.getSynonyms(result, synonyms)
            result.antonyms = mapper.getAntonyms(result, antonyms)
        }
        return result
    }

    override fun deleteRx(t: Word): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Word? {
        return dao.getItem(id)
    }

    override fun getItemRx(word: String, full: Boolean): Maybe<Word> {
        return dao.getItemRx(word)
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
        return dao.getItemsRx(ids)    }

    override fun getItemsRx(bitmap: Bitmap): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Int): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchItems(query: String, limit: Int): List<Word> {
        return dao.getSearchItems(query, limit)
    }

    override fun getCommonItems(): List<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAlphaItems(): List<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRawWords(): List<String> {
        return dao.getRawItems()    }

    override fun getRawWordsRx(): Maybe<List<String>> {
        return Maybe.create { emitter ->
            val result = getRawWords()
            if (emitter.isDisposed) {
                return@create
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    override fun isEmpty(): Boolean {
        return getCount() == 0
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        return Maybe.fromCallable({ this.isEmpty() })
    }

    override fun getCount(): Int {
        return dao.count
    }

    override fun getCountRx(): Maybe<Int> {
        return dao.countRx
    }

    override fun isExists(t: Word): Boolean {
        return dao.getCount(t.id) > 0
    }

    override fun isExistsRx(t: Word): Maybe<Boolean> {
        return Maybe.fromCallable { isExists(t) }
    }

    override fun putItem(t: Word): Long {
        val result = dao.insertOrReplace(t)
        if (result != -1L) {
            val synonyms = mapper.getSynonyms(t)
            val antonyms = mapper.getAntonyms(t)
            if (!DataUtil.isEmpty(synonyms)) {
                synonymDao.insertOrReplace(synonyms!!)
            }
            if (!DataUtil.isEmpty(antonyms)) {
                antonymDao.insertOrReplace(antonyms!!)
            }
        }
        return result
    }

    override fun putItemRx(t: Word): Maybe<Long> {
        return Maybe.fromCallable {
            putItem(t)
        }
    }

    override fun putItems(ts: List<Word>): List<Long> {
        val result = dao.insertOrIgnore(ts)
/*        Stream.of(words).forEach(coin -> {
            if (!isExists(coin)) {
                result.add(putItem(coin));
            }
        });*/
        val count = getCount()
        //Timber.v("Input Words %d Inserted Words %d total %d", words.size(), result.size(), count);
        return result
    }

    override fun putItemsRx(ts: List<Word>): Maybe<List<Long>> {
        return Maybe.fromCallable { putItems(ts) }
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
}