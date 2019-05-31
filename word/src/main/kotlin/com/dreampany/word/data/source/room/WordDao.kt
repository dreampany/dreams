package com.dreampany.word.data.source.room

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.word.data.model.Word
import io.reactivex.Maybe


/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Dao
interface WordDao: BaseDao<Word> {
    @get:Query("select count(*) from word")
    val count: Int

    @get:Query("select count(*) from word")
    val countRx: Maybe<Int>

    @get:Query("select * from word order by word asc")
    val items: List<Word>

    @get:Query("select * from word order by word asc")
    val itemsRx: Maybe<List<Word>>

    @Query("select count(*) from word where word = :word limit 1")
    fun getCount(word: String): Int

    @Query("select count(*) from word where word = :word limit 1")
    fun getCountRx(word: String): Maybe<Int>

    @Query("select * from word where id = :id limit 1")
    fun getItem(id: Long): Word

    @Query("select * from word where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<Word>

    @Query("select * from word where word = :word limit 1")
    fun getItem(word: String): Word

    @Query("select * from word where word = :word limit 1")
    fun getItemRx(word: String): Maybe<Word>

    @Query("select * from word where word like :query || '%' order by word asc")
    fun getSearchItems(query: String): List<Word>

    @Query("select * from word where word like :query || '%' order by word asc limit :limit")
    fun getSearchItems(query: String, limit: Int): List<Word>
}