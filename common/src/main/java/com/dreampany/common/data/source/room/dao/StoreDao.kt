package com.dreampany.common.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.model.Store

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface StoreDao : BaseDao<Store> {
    @get:Query("select count(*) from store")
    val count: Int

    @get:Query("select * from store")
    val items: List<Store>?

    @Query("select count(*) from store where id = :id and type = :type and subtype = :subtype and state = :state")
    fun readCount(id: String, type: String, subtype: String, state: String): Int

    @Query("select * from store where id = :id and type = :type and subtype = :subtype and state = :state limit 1")
    fun read(id: String, type: String, subtype: String, state: String): Store?

    @Query("select * from store where type = :type and subtype = :subtype and state = :state")
    fun reads(type: String, subtype: String, state: String): List<Store>?

    /*@Query("delete from store where id = :id and type = :type and subtype = :subtype and state = :state")
    fun delete(id: String, type: String, subtype: String, state: String): Int*/
}