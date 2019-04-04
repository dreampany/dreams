package com.dreampany.frame.data.source.local

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.data.source.dao.BaseDao
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Dao
interface StoreDao : BaseDao<Store> {

    @get:Query("select count(*) from store")
    val count: Long

    @get:Query("select count(*) from store")
    val countRx: Maybe<Long>

    @get:Query("select * from store")
    val items: List<Store>

    @get:Query("select * from store")
    val itemsRx: Maybe<List<Store>>

    @Query("select count(*) from store where id = :id and type = :type and subtype = :subtype")
    fun getCount(id: Long, type: String, subtype: String): Long

    @Query("select count(*) from store where id = :id and type = :type and subtype = :subtype")
    fun getCountRx(id: Long, type: String, subtype: String): Maybe<Long>

    @Query("select * from store where id = :id limit 1")
    fun getItem(id: Long): Store

    @Query("select * from store where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<Store>

    @Query("select * from store where id = :id and type = :type and subtype = :subtype limit 1")
    fun getItem(id: Long, type: String, subtype: String): Store

    @Query("select * from store where id = :id and type = :type and subtype = :subtype limit 1")
    fun getItemRx(id: Long, type: String, subtype: String): Maybe<Store>

    @Query("select * from store limit :limit")
    fun getItems(limit: Int): List<Store>

    @Query("select * from store limit :limit")
    fun getItemsRx(limit: Int): Maybe<List<Store>>

    @Query("select data from store where type = :type and subtype = :subtype and state = :state order by time desc")
    fun getItemsOf(type: String, subtype: String, state: String): List<String>

    @Query("select data from store where type = :type and subtype = :subtype and state = :state order by time desc")
    fun getItemsOfRx(type: String, subtype: String, state: String): Maybe<List<String>>

    @Query("select data from store where type = :type and subtype = :subtype and state = :state order by time desc limit :limit")
    fun getItemsOfRx(type: String, subtype: String, state: String, limit: Int): Maybe<List<String>>

    @Query("select * from store where type = :type and subtype = :subtype and state = :state")
    fun getItems(type: String, subtype: String, state: String): List<Store>

    @Query("select * from store where type = :type and subtype = :subtype and state = :state")
    fun getItemsRx(type: String, subtype: String, state: String): Maybe<List<Store>>

    @Query("select * from store where type = :type and subtype = :subtype and state = :state limit :limit")
    fun getItems(type: String, subtype: String, state: String, limit: Int): List<Store>

    @Query("select * from store where type = :type and subtype = :subtype and state = :state limit :limit")
    fun getItemsRx(type: String, subtype: String, state: String, limit: Int): Maybe<List<Store>>
}