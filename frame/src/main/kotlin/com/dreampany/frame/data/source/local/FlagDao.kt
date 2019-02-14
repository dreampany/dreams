/*
package com.dreampany.frame.data.source.local

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.frame.data.model.Flag
import io.reactivex.Maybe

*/
/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*


@Dao
interface FlagDao : BaseDao<Flag> {

    @get:Query("select count(*) from favorite")
    val count: Int

    @get:Query("select count(*) from favorite")
    val countRx: Maybe<Int>

    @get:Query("select * from favorite")
    val items: List<Flag>

    @get:Query("select * from favorite")
    val itemsRx: Maybe<List<Flag>>

    @Query("select count(*) from favorite where id = :id and type = :type and subtype = :subtype")
    fun getCount(id: Long, type: String, subtype: String): Int

    @Query("select count(*) from favorite where id = :id and type = :type and subtype = :subtype")
    fun getCountRx(id: Long, type: String, subtype: String): Maybe<Int>

    @Query("select * from favorite where id = :id limit 1")
    fun getItem(id: Long): Flag

    @Query("select * from favorite where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<Flag>

    @Query("select * from favorite where id = :id and type = :type and subtype = :subtype limit 1")
    fun getItem(id: Long, type : String, subtype: String): Flag

    @Query("select * from favorite where id = :id and type = :type and subtype = :subtype limit 1")
    fun getItemRx(id: Long, type : String, subtype: String): Maybe<Flag>

    @Query("select * from favorite limit :limit")
    fun getItems(limit: Int): List<Flag>

    @Query("select * from favorite limit :limit")
    fun getItemsRx(limit: Int): Maybe<List<Flag>>

    @Query("select * from favorite where type = :type and subtype = :subtype")
    fun getItems(type: String, subtype: String): List<Flag>

    @Query("select * from favorite where type = :type and subtype = :subtype")
    fun getItemsRx(type: String, subtype: String): Maybe<List<Flag>>

    @Query("select * from favorite where type = :type and subtype = :subtype limit :limit")
    fun getItems(type: String, subtype: String, limit: Int): List<Flag>

    @Query("select * from favorite where type = :type and subtype = :subtype limit :limit")
    fun getItemsRx(type: String, subtype: String, limit: Int): Maybe<List<Flag>>
}*/
