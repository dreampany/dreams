package com.dreampany.lca.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
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

    @Query("select * from quote where id = :id and currency = :currency limit 1")
    fun getItems(id: String, currency: String): Quote

    @Query("select * from quote where id = :id and currency in (:currencies)")
    fun getItems(id: String, currencies: Array<String>): List<Quote>
}
