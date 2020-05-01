package com.dreampany.common.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.model.Store

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface StoreDao : BaseDao<Store> {
    @get:Query("select count(*) from store")
    val count: Int

    @get:Query("select * from store")
    val items: List<Store>?

    @Query("select count(*) from store where id = :id and type = :type and subtype = :subtype and state = :state")
    fun getCount(id: String, type: String, subtype: String, state: String): Int

    @Query("select * from store where id = :id and type = :type and subtype = :subtype and state = :state limit 1")
    fun getItem(id: String, type: String, subtype: String, state: String): Store?

    @Query("select id from store where type = :type and subtype = :subtype and state = :state")
    fun getItems(type: String, subtype: String, state: String): List<String>?
}