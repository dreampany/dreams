package com.dreampany.tools.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.Coin
import io.reactivex.Maybe

/**
 * Created by roman on 2019-11-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface CoinDao : BaseDao<Coin> {

    @get:Query("select count(*) from coin")
    val count: Int

    @get:Query("select * from coin")
    val items: List<Coin>?

    @get:Query("select * from coin")
    val itemsRx: Maybe<List<Coin>>

    @Query("select * from coin where id = :id limit 1")
    fun getItem(id: String): Coin

}