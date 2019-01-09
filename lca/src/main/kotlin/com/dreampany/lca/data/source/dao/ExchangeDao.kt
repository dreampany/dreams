package com.dreampany.lca.data.source.dao

/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.Exchange
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface ExchangeDao : BaseDao<Exchange> {

    @get:Query("select count(*) from exchange")
    val count: Int

    @get:Query("select count(*) from exchange")
    val countRx: Single<Int>

    @get:Query("select * from exchange")
    val items: List<Exchange>

    @get:Query("select * from exchange")
    val itemsRx: Flowable<List<Exchange>>

    @Query("select count(*) from exchange where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from exchange where id = :id limit 1")
    fun getCountRx(id: Long): Maybe<Int>

    @Query("select * from exchange where id = :id limit 1")
    fun getItem(id: Long): Exchange

    @Query("select * from exchange where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<Exchange>

    @Query("select * from exchange where exchange = :exchange and fromSymbol = :fromSymbol and toSymbol = :toSymbol limit 1")
    fun getItem(exchange: String, fromSymbol: String, toSymbol: String): Exchange

    @Query("select * from exchange where exchange = :exchange and fromSymbol = :fromSymbol and toSymbol = :toSymbol limit 1")
    fun getItemRx(exchange: String, fromSymbol: String, toSymbol: String): Maybe<Exchange>
}
