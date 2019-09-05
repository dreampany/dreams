package com.dreampany.tools.data.source.assets

import android.content.Context
import android.graphics.Bitmap
import com.dreampany.framework.util.DataUtil
import com.dreampany.framework.util.FileUtil
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.api.WordDataSource
import com.dreampany.tools.misc.Constants
import io.reactivex.Maybe
import timber.log.Timber
import java.util.*

/**
 * Created by roman on 2019-08-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AssetsWordDataSource(
    private val context: Context,
    private val mapper: WordMapper
) : WordDataSource {
    override fun getRawItemsByLength(id: String, limit: Int): List<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isValid(id: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val alphaWords = mutableListOf<String>()

    override fun isExists(id: String): Boolean {
        getAlphaRawWords()
        return alphaWords.contains(id)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Int): Maybe<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchItems(query: String, limit: Int): List<Word>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommonItems(): List<Word>? {
        val items = getCommonRawWords()
        val result = mutableListOf<Word>()
        items?.forEach { item ->
            mapper.getItem(item)?.run {
                result.add(this)
            }
        }
        return result
    }

    override fun getAlphaItems(): List<Word>? {
        val items = getAlphaRawWords()
        val result = mutableListOf<Word>()
        items?.forEach { item ->
            mapper.getItem(item)?.run {
                result.add(this)
            }
        }
        return result
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Synchronized
    private fun getCommonRawWords(): List<String>? {
        val items = FileUtil.readAssetsAsStrings(context, Constants.Assets.WORDS_COMMON)
        if (items == null) {
            Timber.v("Assets common words empty")
        }
        return items
    }

    @Synchronized
    private fun getAlphaRawWords(): List<String>? {
        if (DataUtil.isEmpty(alphaWords)) {
            val items = FileUtil.readAssetsAsStrings(context, Constants.Assets.WORDS_ALPHA)
            alphaWords.addAll(items!!)
        }
        return ArrayList(alphaWords)
    }

}