package com.dreampany.word.data.source.api

import android.graphics.Bitmap
import com.dreampany.frame.data.source.api.DataSource
import com.dreampany.word.data.model.Word
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 7/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface WordDataSource : DataSource<Word> {

    fun isExists(word: String): Boolean

    fun getTodayItem(): Word?

    fun getTodayItemRx(): Maybe<Word>

    fun getItem(word: String, full: Boolean): Word?

    fun getItemRx(word: String, full: Boolean): Maybe<Word>

    fun getItems(ids: List<String>): List<Word>

    fun getItemsRx(ids: List<String>): Maybe<List<Word>>

    fun getSearchItems(query: String, limit: Int): List<Word>

    fun getCommonItems(): List<Word>

    fun getAlphaItems(): List<Word>

    fun getItemsRx(bitmap: Bitmap): Maybe<List<Word>>

    fun getRawWords(): List<String>

    fun getRawWordsRx(): Maybe<List<String>>
}