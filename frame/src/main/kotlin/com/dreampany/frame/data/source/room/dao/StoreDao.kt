package com.dreampany.frame.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.model.Store
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Dao
interface StoreDao : BaseDao<Store> {

    @get:Query("select count(*) from store")
    val count: Int

    @get:Query("select count(*) from store")
    val countRx: Maybe<Int>

    @get:Query("select * from store")
    val items: List<Store>?

    @get:Query("select * from store")
    val itemsRx: Maybe<List<Store>>

    @Query("select count(*) from store where id = :id and type = :type and subtype = :subtype")
    fun getCount(id: String, type: String, subtype: String): Int

    @Query("select count(*) from store where id = :id and type = :type and subtype = :subtype and state = :state")
    fun getCount(id: String, type: String, subtype: String, state: String): Int

    @Query("select count(*) from store where type = :type and subtype = :subtype and state = :state")
    fun getCountByType(type: String, subtype: String, state: String): Int

    @Query("select count(*) from store where id = :id and type = :type and subtype = :subtype")
    fun getCountRx(id: String, type: String, subtype: String): Maybe<Int>

    @Query("select * from store where id = :id limit 1")
    fun getItem(id: String): Store?

    @Query("select * from store where id = :id limit 1")
    fun getItemRx(id: String): Maybe<Store>

    @Query("select * from store where type = :type and subtype = :subtype and state = :state limit 1")
    fun getItem(type: String, subtype: String, state: String): Store?

    @Query("select * from store where type = :type and subtype = :subtype and state = :state order by random() limit 1")
    fun getRandomItem(type: String, subtype: String, state: String): Store?

    @Query("select * from store where type = :type and subtype = :subtype and state = :state limit 1")
    fun getItemRx(type: String, subtype: String, state: String): Maybe<Store>

    @Query("select * from store where id = :id and type = :type and subtype = :subtype and state = :state limit 1")
    fun getItem(id: String, type: String, subtype: String, state: String): Store?

    @Query("select * from store where id = :id and type = :type and subtype = :subtype and state = :state limit 1")
    fun getItemRx(id: String, type: String, subtype: String, state: String): Maybe<Store>

    @Query("select * from store limit :limit")
    fun getItems(limit: Int): List<Store>?

    @Query("select * from store limit :limit")
    fun getItemsRx(limit: Int): Maybe<List<Store>>

    @Query("select data from store where type = :type and subtype = :subtype and state = :state order by time desc")
    fun getItemsOf(type: String, subtype: String, state: String): List<String>?

    @Query("select data from store where type = :type and subtype = :subtype and state = :state order by time desc")
    fun getItemsOfRx(type: String, subtype: String, state: String): Maybe<List<String>>

    @Query("select data from store where type = :type and subtype = :subtype and state = :state order by time desc limit :limit")
    fun getItemsOfRx(type: String, subtype: String, state: String, limit: Int): Maybe<List<String>>

    @Query("select * from store where type = :type and subtype = :subtype and state = :state")
    fun getItems(type: String, subtype: String, state: String): List<Store>?

    @Query("select * from store where type = :type and subtype = :subtype and state = :state")
    fun getItemsRx(type: String, subtype: String, state: String): Maybe<List<Store>>

    @Query("select * from store where type = :type and subtype = :subtype and state = :state limit :limit")
    fun getItems(type: String, subtype: String, state: String, limit: Int): List<Store>?

    @Query("select * from store where type = :type and subtype = :subtype and state = :state limit :limit")
    fun getItemsRx(type: String, subtype: String, state: String, limit: Int): Maybe<List<Store>>
}