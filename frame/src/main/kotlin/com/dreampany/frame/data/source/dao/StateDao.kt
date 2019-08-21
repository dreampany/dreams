/*
package com.dreampany.frame.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.model.State
import io.reactivex.Maybe


*/
/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

@Dao
interface StateDao : BaseDao<State> {

    @get:Query("select count(*) from state")
    val count: Int

    @get:Query("select count(*) from state")
    val countRx: Maybe<Int>

    @get:Query("select * from state")
    val items: List<State>

    @get:Query("select * from state")
    val itemsRx: Maybe<List<State>>

    @Query("select count(*) from state where id = :id and type = :type and subtype = :subtype")
    fun getCountById(id: String, type: String, subtype: String): Int

    @Query("select count(*) from state where type = :type and subtype = :subtype and state = :state")
    fun getCount(type: String, subtype: String, state: String): Int

    @Query("select count(*) from state where type = :type and subtype = :subtype and state = :state")
    fun getCountRx(type: String, subtype: String, state: String): Maybe<Int>

    @Query("select count(*) from state where id = :id and type = :type and subtype = :subtype and state = :state")
    fun getCount(id: String, type: String, subtype: String, state: String): Int

    @Query("select count(*) from state where id = :id and type = :type and subtype = :subtype and state = :state")
    fun getCountRx(id: String, type: String, subtype: String, state: String): Maybe<Int>

    @Query("select * from state where id = :id limit 1")
    fun getItem(id: String): State?

    @Query("select * from state where id = :id limit 1")
    fun getItemRx(id: String): Maybe<State>

    @Query("select * from state where type = :type and subtype = :subtype and state != :state order by time desc limit 1")
    fun getItemNotStateOrderByRx(type: String, subtype: String, state: String): Maybe<State>

    @Query("select * from state where type = :type and subtype = :subtype and state = :state and time between :to and :from order by time desc limit 1")
    fun getItemOrderByRx(
        type: String,
        subtype: String,
        state: String,
        from: Long,
        to: Long
    ): Maybe<State>

    @Query("select * from state limit :limit")
    fun getItems(limit: Int): List<State>?

    @Query("select * from state limit :limit")
    fun getItemsRx(limit: Int): Maybe<List<State>>

    @Query("select * from state where type = :type and subtype = :subtype and state = :state")
    fun getItemsWithoutId(type: String, subtype: String, state: String): List<State>?

    @Query("select * from state where id = :id and type = :type and subtype = :subtype")
    fun getItems(id: Long, type: String, subtype: String): List<State>?

    @Query("select * from state where type = :type and subtype = :subtype and state = :state")
    fun getItemsRx(type: String, subtype: String, state: String): Maybe<List<State>>

    @Query("select * from state where type = :type and subtype = :subtype and state = :state order by time desc")
    fun getItemsOrderBy(type: String, subtype: String, state: String): List<State>?

    @Query("select * from state where type = :type and subtype = :subtype and state = :state order by time desc")
    fun getItemsOrderByRx(type: String, subtype: String, state: String): Maybe<List<State>>

    @Query("select * from state where type = :type and subtype = :subtype order by time desc")
    fun getItemsOrderBy(type: String, subtype: String): List<State>?

    @Query("select * from state where type = :type and subtype = :subtype order by time desc")
    fun getItemsOrderByRx(type: String, subtype: String): Maybe<List<State>>

    @Query("select * from state where type = :type and subtype = :subtype order by time desc limit :limit")
    fun getItemsOrderBy(type: String, subtype: String, limit: Int): List<State>?

    @Query("select * from state where type = :type and subtype = :subtype order by time desc limit :limit")
    fun getItemsOrderByRx(type: String, subtype: String, limit: Int): Maybe<List<State>>

    @Query("select * from state where type = :type and subtype = :subtype and state = :state order by time desc limit :limit")
    fun getItemsOrderBy(type: String, subtype: String, state: String, limit: Int): List<State>?

    @Query("select * from state where type = :type and subtype = :subtype and state = :state order by time desc limit :limit")
    fun getItemsOrderByRx(
        type: String,
        subtype: String,
        state: String,
        limit: Int
    ): Maybe<List<State>>

    @Query("select * from state where id = :id and type = :type and subtype = :subtype and state = :state limit 1")
    fun getItem(id: String, type: String, subtype: String, state: String): State?

    @Query("select * from state where id = :id and type = :type and subtype = :subtype and state = :state limit 1")
    fun getItemRx(id: String, type: String, subtype: String, state: String): Maybe<State>

    @Query("select * from state where type = :type and subtype = :subtype and state = :state limit 1")
    fun getItem(type: String, subtype: String, state: String): State?

    @Query("select * from state where type = :type and subtype = :subtype and state = :state limit 1")
    fun getItemRx(type: String, subtype: String, state: String): Maybe<State>

    @Query("select * from state where type = :type and subtype = :subtype")
    fun getItems(type: String, subtype: String): List<State>?

    @Query("select * from state where type = :type and subtype = :subtype")
    fun getItemsRx(type: String, subtype: String): Maybe<List<State>>

    @Query("select * from state where type = :type and subtype = :subtype and time between :to and :from order by time desc")
    fun getItemsOrderBy(type: String, subtype: String, from: Long, to: Long): List<State>?

    @Query("select * from state where type = :type and subtype = :subtype and time between :to and :from order by time desc")
    fun getItemsOrderByRx(type: String, subtype: String, from: Long, to: Long): Maybe<List<State>>

    @Query("select * from state where type = :type and subtype = :subtype limit :limit")
    fun getItems(type: String, subtype: String, limit: Int): List<State>?

    @Query("select * from state where type = :type and subtype = :subtype limit :limit")
    fun getItemsRx(type: String, subtype: String, limit: Int): Maybe<List<State>>
}*/
