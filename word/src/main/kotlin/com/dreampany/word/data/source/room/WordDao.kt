package com.dreampany.word.data.source.room

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.word.data.model.Word
import io.reactivex.Maybe
import java.util.ArrayList


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

    @get:Query("select * from word order by id asc")
    val items: List<Word>

    @get:Query("select * from word order by id asc")
    val itemsRx: Maybe<List<Word>>

    @Query("select count(*) from word where id = :id limit 1")
    fun getCount(id: String): Int

    @Query("select count(*) from word where id = :id limit 1")
    fun getCountRx(id: String): Maybe<Int>

    @Query("select * from word where id = :id limit 1")
    fun getItem(id: String): Word

    @Query("select * from word where id = :id limit 1")
    fun getItemRx(id: String): Maybe<Word>

    @Query("select * from word where id in (:ids)")
    fun getItemsRx(ids: List<String>): Maybe<List<Word>>

    @Query("select * from word where id like :query || '%' order by id asc")
    fun getSearchItems(query: String): List<Word>

    @Query("select * from word where id like :query || '%' order by id asc limit :limit")
    fun getSearchItems(query: String, limit: Int): List<Word>

    @Query("select id from word order by id asc")
    fun getRawItems(): List<String>
}