package com.dreampany.lca.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.CoinAlarm
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface CoinAlarmDao : BaseDao<CoinAlarm> {

    @get:Query("select count(*) from coinalarm")
    val count: Int

    @get:Query("select count(*) from coinalarm")
    val countRx: Maybe<Int>

    @get:Query("select * from coinalarm")
    val items: List<CoinAlarm>

    @get:Query("select * from coinalarm")
    val itemsRx: Maybe<List<CoinAlarm>>

    @Query("select count(*) from coinalarm where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from coinalarm where id = :id limit 1")
    fun getCountRx(id: Long): Maybe<Int>

    @Query("select * from coinalarm where id = :id limit 1")
    fun getItem(id: Long): CoinAlarm

    @Query("select * from coinalarm where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<CoinAlarm>

    @Query("select * from coinalarm limit :limit")
    fun getItems(status: String, limit: Int): List<CoinAlarm>

    @Query("select * from coinalarm limit :limit")
    fun getItemsRx(status: String, limit: Int): Maybe<List<CoinAlarm>>
}
