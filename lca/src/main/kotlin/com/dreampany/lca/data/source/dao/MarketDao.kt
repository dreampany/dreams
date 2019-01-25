package com.dreampany.lca.data.source.dao


import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.Market
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Dao
interface MarketDao : BaseDao<Market> {

    @get:Query("select count(*) from market")
    val count: Int

    @get:Query("select count(*) from market")
    val countRx: Single<Int>

    @get:Query("select * from market")
    val items: List<Market>

    @get:Query("select * from market")
    val itemsRx: Flowable<List<Market>>

    @Query("select count(*) from market where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from market where id = :id limit 1")
    fun getCountRx(id: Long): Single<Int>

    @Query("select * from market where id = :id limit 1")
    fun getItem(id: Long): Market

    @Query("select * from market where id = :id limit 1")
    fun getItemRx(id: Long): Single<Market>

    @Query("select * from market where market = :market and fromSymbol = :fromSymbol and toSymbol = :toSymbol limit 1")
    fun getItem(market: String, fromSymbol: String, toSymbol: String): Market

    @Query("select * from market where market = :market and fromSymbol = :fromSymbol and toSymbol = :toSymbol limit 1")
    fun getItemRx(market: String, fromSymbol: String, toSymbol: String): Single<Market>
}
