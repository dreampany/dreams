package com.dreampany.lca.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.Price
import io.reactivex.Flowable
import io.reactivex.Single


/**
 * Created by Hawladar Roman on 7/23/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface PriceDao : BaseDao<Price> {
    @get:Query("select count(*) from price")
    val count: Int

    @get:Query("select count(*) from price")
    val countRx: Single<Int>

    @get:Query("select * from price")
    val items: List<Price>

    @get:Query("select * from price")
    val itemsRx: Flowable<List<Price>>

    @Query("select count(*) from price where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from price where id = :id limit 1")
    fun getCountRx(id: Long): Single<Int>

    @Query("select * from price where id = :id limit 1")
    fun getItem(id: Long): Price

    @Query("select * from price where id = :id limit 1")
    fun getItemRx(id: Long): Single<Price>

    @Query("select * from price limit :limit")
    fun getItems(limit: Int): List<Price>

    @Query("select * from price limit :limit")
    fun getItemsRx(limit: Int): Flowable<List<Price>>
}