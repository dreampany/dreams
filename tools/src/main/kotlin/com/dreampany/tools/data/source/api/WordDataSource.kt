package com.dreampany.tools.data.source.api

import android.graphics.Bitmap
import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.tools.data.model.Word
import io.reactivex.Maybe

/**
 * Created by roman on 2019-08-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface WordDataSource : DataSource<Word> {

    fun isValid(id: String): Boolean

    fun isExists(id: String): Boolean

    fun getItems(ids: List<String>): List<Word>?

    fun getItemsRx(ids: List<String>): Maybe<List<Word>>

    fun getSearchItems(query: String, limit: Long): List<Word>?

    fun getCommonItems(): List<Word>?

    fun getAlphaItems(): List<Word>?

    fun getItemsRx(bitmap: Bitmap): Maybe<List<Word>>

    fun getRawWords(): List<String>?

    fun getRawWordsRx(): Maybe<List<String>>

    fun getRawItemsByLength(id: String, limit: Long): List<String>?

    fun track(word: Word): Long

    fun trackRx(word: Word): Maybe<Long>

    fun getTracks(startAt: Long, limit: Long): List<String>?

    fun getTracksRx(startAt: Long, limit: Long): Maybe<List<String>>
}