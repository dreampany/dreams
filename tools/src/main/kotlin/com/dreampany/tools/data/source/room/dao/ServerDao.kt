package com.dreampany.tools.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.model.Server
import io.reactivex.Maybe

/**
 * Created by roman on 2019-10-08
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface ServerDao : BaseDao<Server> {

    @Query("delete from server")
    fun deleteAll()

    @Query("select * from server where id = :id limit 1")
    fun getItem(id: String): Server?

    @Query("select * from server where id = :id limit 1")
    fun getItemRx(id: String): Maybe<Server>

    @Query("select * from server")
    fun getItems(): List<Server>?

    @Query("select * from server limit :limit")
    fun getItemsRx(limit: Long): Maybe<List<Server>>

    @Query("select * from server order by random() limit 1")
    fun getRandomItem(): Server?

    @Query("select * from server order by random() limit 1")
    fun getRandomItemRx(): Maybe<Server>

    @Query(value = "select * from server where country_code = :countryCode")
    fun getServersRx(countryCode: String): Maybe<List<Server>>
}