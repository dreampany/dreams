package com.dreampany.lca.data.source.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.Coin
import com.dreampany.lca.data.model.Quote
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Dao
interface QuoteDao : BaseDao<Quote> {

    @get:Query("select count(*) from quote")
    val count: Int

    @get:Query("select count(*) from quote")
    val countRx: Maybe<Int>

    @get:Query("select * from quote")
    val items: List<Quote>

    @get:Query("select * from quote")
    val itemsRx: Maybe<List<Quote>>

    @Query("select count(*) from quote where id = :id and currency = :currency limit 1")
    fun getCount(id: Long, currency: String): Int

    @Query("select count(*) from quote where id = :id and currency = :currency limit 1")
    fun getCountRx(id: Long, currency: String): Maybe<Int>

    @Query("select * from quote where id = :id and currency = :currency limit 1")
    fun getItem(id: Long, currency: String): Coin

    @Query("select * from quote where id = :id and currency = :currency limit 1")
    fun getItemRx(id: Long, currency: String): Maybe<Coin>
}
