package com.dreampany.tools.data.source.repository

import android.graphics.Bitmap
import com.dreampany.frame.data.source.repository.Repository
import com.dreampany.frame.misc.*
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.api.WordDataSource
import io.reactivex.Maybe
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
    @Assets private val assets: WordDataSource,
    @Room private val room: WordDataSource,
    @Firestore private val firestore: WordDataSource,
    @Remote private val remote: WordDataSource,
    @Vision private val vision: WordDataSource
) : Repository<String, Word>(rx, rm), WordDataSource {
    override fun isExists(word: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: Word): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTodayItem(): Word? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTodayItemRx(): Maybe<Word> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(word: String, full: Boolean): Word? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun delete(ts: List<Word>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Word>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}