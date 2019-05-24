package com.dreampany.lca.data.source.dao


import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.Coin
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Dao
interface CoinDao : BaseDao<Coin> {

    @get:Query("select count(*) from coin")
    val count: Int

    @get:Query("select count(*) from coin")
    val countRx: Maybe<Int>

    @get:Query("select * from coin order by rank asc")
    val items: List<Coin>

    @get:Query("select * from coin order by rank asc")
    val itemsRx: Maybe<List<Coin>>

    @Query("select count(*) from coin where id = :id limit 1")
    fun getCount(id: Long): Int

    @Query("select count(*) from coin where id = :id limit 1")
    fun getCountRx(id: Long): Maybe<Int>

    @Query("select * from coin where id = :id limit 1")
    fun getItem(id: Long): Coin

    @Query("select * from coin where id = :id limit 1")
    fun getItemRx(id: Long): Maybe<Coin>

    @Query("select * from coin where symbol = :symbol limit 1")
    fun getItem(symbol: String): Coin

    @Query("select * from coin where symbol = :symbol and lastUpdated  <= :lastUpdated limit 1")
    fun getItem(symbol: String, lastUpdated: Long): Coin

    @Query("select * from coin where symbol = :symbol and lastUpdated  <= :lastUpdated limit 1")
    fun getItemRx(symbol: String, lastUpdated: Long): Maybe<Coin>

/*    @Query("select * from coin where coinId = :coinId limit 1")
    fun getItemByCoinId(coinId: Long): Coin*/

/*    @Query("select * from coin where coinId = :coinId limit 1")
    fun getItemByCoinIdRx(coinId: Long): Maybe<Coin>*/

    @Query("select * from coin where rank >= :start order by rank asc")
    fun getItems(start: Int): List<Coin>

    @Query("select * from coin where rank >= :start order by rank asc")
    fun getItemsRx(start: Int): Maybe<List<Coin>>

    @Query("select * from coin where rank >= :start and rank < :end order by rank asc limit :limit")
    fun getItems(start: Int, end: Int, limit: Int): List<Coin>

    @Query("select * from coin where symbol in (:symbols) order by rank asc")
    fun getItems(symbols: List<String>): List<Coin>

/*    @Query("select * from coin where coinId = :coinId and lastUpdated >= :lastUpdated order by rank asc limit 1")
    fun getItem(coinId: Long, lastUpdated: Long): Coin*/

/*    @Query("select * from coin where coinId in (:coinIds) and lastUpdated >= :lastUpdated order by rank asc")
    fun getItems(coinIds: List<Long>, lastUpdated: Long): List<Coin>*/

/*    @Query("select * from coin where coinId in (:coinIds) and lastUpdated >= :lastUpdated order by rank asc")
    fun getItemsRx(coinIds: List<Long>, lastUpdated: Long): Maybe<List<Coin>>*/

    @Query("select * from coin where rank >= :start order by rank asc limit :limit")
    fun getItemsRx(start: Int, limit: Int): Maybe<List<Coin>>

    @Query("select * from coin where rank >= :start and rank < :end order by rank asc limit :limit")
    fun getItemsRx(start: Int, end: Int, limit: Int): Maybe<List<Coin>>
}
