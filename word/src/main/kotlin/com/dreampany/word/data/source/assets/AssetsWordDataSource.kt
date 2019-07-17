package com.dreampany.word.data.source.assets

import android.content.Context
import android.graphics.Bitmap
import com.annimon.stream.Stream
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.FileUtil
import com.dreampany.word.data.misc.WordMapper
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.api.WordDataSource
import com.dreampany.word.misc.Constants
import io.reactivex.Maybe
import timber.log.Timber
import java.util.*
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AssetsWordDataSource(
    val context: Context,
    val mapper: WordMapper
) : WordDataSource {

    private val alphaWords = mutableListOf<String>()


    override fun getTodayItem(): Word {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTodayItemRx(): Maybe<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(word: String, full: Boolean): Word? {
        loadWords()
        return if (alphaWords.contains(word)) mapper.toItem(word) else null
    }

    override fun deleteRx(t: Word): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Word? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        val items = getCommonWords()
        val result = ArrayList<Word>()
        Stream.of(Objects.requireNonNull<List<String>>(items)).forEach { item ->
            val word = mapper.toItem(item)
            result.add(word)
        }
        return result
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
        return Maybe.fromCallable({ this.getItems() })
    }

    override fun getItemsRx(limit: Int): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchItems(query: String, limit: Int): List<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommonItems(): List<Word> {
        val items = getCommonWords()
        val result = mutableListOf<Word>()
        Stream.of(Objects.requireNonNull<List<String>>(items)).forEach { item ->
            val word = mapper.toItem(item)
            result.add(word)
        }
        return result
    }

    override fun getAlphaItems(): List<Word> {
        val items = getAlphaWords()
        val result = mutableListOf<Word>()
        Stream.of(Objects.requireNonNull<List<String>>(items)).forEach { item ->
            val word = mapper.toItem(item)
            result.add(word)
        }
        return result    }

    override fun getRawWords(): List<String> {
        return getAlphaWords()
    }

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

    override fun isExists(t: Word): Boolean {
        val items = getAlphaWords()
        return items.contains(t.id)
    }

    override fun isExistsRx(t: Word): Maybe<Boolean> {
        return Maybe.fromCallable { isExists(t) }
    }

    override fun putItem(t: Word): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: Word): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Word>): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Word>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    @Synchronized
    private fun getCommonWords(): List<String>? {
        val items = FileUtil.readAssetsAsStrings(context, Constants.Assets.WORDS_COMMON)
        if (items == null) {
            Timber.v("Assets common words empty")
        }
        return items
    }

    @Synchronized
    private fun getAlphaWords(): List<String> {
        if (DataUtil.isEmpty(alphaWords)) {
            val items = FileUtil.readAssetsAsStrings(context, Constants.Assets.WORDS_ALPHA)
            alphaWords.addAll(items!!)
        }
        return ArrayList(alphaWords)
    }

    private fun loadWords() {
        if (DataUtil.isEmpty(alphaWords)) {
            val items = FileUtil.readAssetsAsStrings(context, Constants.Assets.WORDS_ALPHA)
            alphaWords.addAll(items!!)
        }
    }
}